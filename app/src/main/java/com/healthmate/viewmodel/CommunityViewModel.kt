
package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.healthmate.data.local.entity.Community
import com.healthmate.data.local.entity.CommunityMember
import com.healthmate.data.local.entity.CommunityPost
import com.healthmate.data.local.entity.CommunityLike
import com.healthmate.data.local.entity.CommunityComment
import com.healthmate.data.repository.CommunityRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.healthmate.ui.communities.model.CommunityPostUiModel

class CommunityViewModel(
    private val repository: CommunityRepository,
    private val currentUserId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private var selectedCommunityId: Int? = null

    init {
        observeCommunities()
        observeJoinedCommunities()
        observeAllPosts()
    }

    private suspend fun mapPostsToUiModels(
        posts: List<CommunityPost>
    ): List<CommunityPostUiModel> {

        return posts.map { post ->

            val user = repository.getUserById(post.userId)

            val likeCount = _uiState.value.likeCounts[post.id] ?: 0

            val commentCount = repository
                .getCommentCount(post.id)
                .first()

            val isLiked = post.id in _uiState.value.likedPostIds

            val authorName = user?.fullName ?: "HealthMate Member"

            val initials = authorName
                .split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .joinToString("") { it.first().uppercase() }

            CommunityPostUiModel(
                id = post.id,
                authorName = authorName,
                authorInitials = initials,
                timeAgo = post.createdAt,
                location = "Community",
                content = post.content,
                likes = likeCount,
                comments = commentCount,
                shares = 0,
                isLiked = isLiked,
                authorProfileImageUri = user?.profileImageUri,
                postImageUri = post.imageUri,
                userId = post.userId
            )
        }
    }

    // Observe all communities
    private fun observeCommunities() {
        viewModelScope.launch {
            repository.getAllCommunities().collect { communities ->
                _uiState.value = _uiState.value.copy(
                    communities = communities
                )
            }
        }
    }

    // Observe communities joined by current user
    private fun observeJoinedCommunities() {
        viewModelScope.launch {
            repository.getJoinedCommunities(currentUserId).collect { memberships ->
                _uiState.value = _uiState.value.copy(
                    joinedCommunityIds = memberships
                        .map { it.communityId }
                        .toSet()
                )
            }
        }
    }

    // Observe all posts
    private fun observeAllPosts() {
        viewModelScope.launch {
            repository.getAllPosts().collect { posts ->

                _uiState.value = _uiState.value.copy(
                    posts = posts
                )

                // Refresh likes and interaction data
                refreshPostInteractions(posts)

                // Convert Room posts into UI models
                val postUiModels = mapPostsToUiModels(posts)

                _uiState.value = _uiState.value.copy(
                    postUiModels = postUiModels
                )
            }
        }
    }

    // Create a community
    fun createCommunity(
        name: String,
        description: String,
        category: String,
        coverImageUri: String? = null,
        privacy: String = "Public",
        guidelines: String = ""
    ) {
        viewModelScope.launch {
            val cleanName = name.trim()
            val cleanDescription = description.trim()

            if (cleanName.isBlank() || cleanDescription.isBlank()) {
                showError("Please complete all community fields.")
                return@launch
            }

            if (category.isBlank()) {
                showError("Please select a community category.")
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                val communityId = repository.createCommunity(
                    Community(
                        name = cleanName,
                        description = cleanDescription,
                        category = category.trim(),
                        createdBy = currentUserId,
                        createdAt = getCurrentTimestamp(),
                        coverImageUri = coverImageUri,
                        privacy = privacy,
                        guidelines = guidelines.trim()
                    )
                )

                if (communityId > 0) {

                    repository.joinCommunity(
                        CommunityMember(
                            communityId = communityId.toInt(),
                            userId = currentUserId,
                            joinedAt = getCurrentTimestamp()
                        )
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Community created successfully."
                    )

                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unable to create community."
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to create community."
                )
            }
        }
    }

    // Select a community
    fun selectCommunity(community: Community) {
        selectedCommunityId = community.id

        _uiState.value = _uiState.value.copy(
            selectedCommunity = community,
            posts = emptyList(),
            postUiModels = emptyList(),
            comments = emptyList(),
            memberCount = 0
        )

        viewModelScope.launch {

            // Observe this community's member count
            launch {
                repository.getMemberCount(community.id).collect { count ->
                    _uiState.value = _uiState.value.copy(
                        memberCount = count
                    )
                }
            }

            // Reload posts belonging to this community
            launch {
                repository.getPostsByCommunity(community.id).collect { posts ->

                    _uiState.value = _uiState.value.copy(
                        posts = posts
                    )

                    refreshPostInteractions(posts)

                    val postUiModels = mapPostsToUiModels(posts)

                    _uiState.value = _uiState.value.copy(
                        postUiModels = postUiModels
                    )
                }
            }
        }
    }

    // Join a community
    fun joinCommunity(communityId: Int) {
        viewModelScope.launch {
            try {
                repository.joinCommunity(
                    CommunityMember(
                        communityId = communityId,
                        userId = currentUserId,
                        joinedAt = getCurrentTimestamp()
                    )
                )

                _uiState.value = _uiState.value.copy(
                    successMessage = "You joined the community."
                )

            } catch (e: Exception) {
                showError("Unable to join community.")
            }
        }
    }

    // Leave a community
    fun leaveCommunity(communityId: Int) {
        viewModelScope.launch {
            try {
                repository.leaveCommunity(
                    communityId,
                    currentUserId
                )

                _uiState.value = _uiState.value.copy(
                    successMessage = "You left the community."
                )

            } catch (e: Exception) {
                showError("Unable to leave community.")
            }
        }
    }

    // Create a post
    fun createPost(
        communityId: Int,
        content: String,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            val cleanContent = content.trim()

            if (cleanContent.isBlank() && imageUri.isNullOrBlank()) {
                showError("Please add text or a photo before posting.")
                return@launch
            }

            if (!repository.isMember(communityId, currentUserId)) {
                showError("Join this community before posting.")
                return@launch
            }

            try {
                repository.createPost(
                    CommunityPost(
                        communityId = communityId,
                        userId = currentUserId,
                        content = cleanContent,
                        createdAt = getCurrentTimestamp(),
                        imageUri = imageUri
                    )
                )

                _uiState.value = _uiState.value.copy(
                    successMessage = "Post published successfully."
                )

            } catch (e: Exception) {
                showError("Unable to publish post.")
            }
        }
    }

    // Edit an existing post
    fun editPost(
        postId: Int,
        content: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            try {
                val existingPost = repository.getPostById(postId)

                if (existingPost == null || existingPost.userId != currentUserId) {
                    showError("You cannot edit this post.")
                    return@launch
                }

                if (content.isBlank() && imageUri.isNullOrBlank()) {
                    showError("A post must contain text or an image.")
                    return@launch
                }

                repository.updatePost(
                    existingPost.copy(
                        content = content.trim(),
                        imageUri = imageUri
                    )
                )

                _uiState.value = _uiState.value.copy(
                    successMessage = "Post updated successfully."
                )

            } catch (e: Exception) {
                showError("Unable to update post.")
            }
        }
    }

    // Delete an existing post
    fun deletePost(postId: Int) {
        viewModelScope.launch {
            try {
                val existingPost = repository.getPostById(postId)

                if (existingPost == null || existingPost.userId != currentUserId) {
                    showError("You cannot delete this post.")
                    return@launch
                }

                repository.deletePost(existingPost)

                _uiState.value = _uiState.value.copy(
                    successMessage = "Post deleted successfully."
                )

            } catch (e: Exception) {
                showError("Unable to delete post.")
            }
        }
    }

    // Like or unlike a post
    fun toggleLike(postId: Int) {
        viewModelScope.launch {
            try {
                val alreadyLiked = repository.hasUserLikedPost(
                    postId,
                    currentUserId
                )

                if (alreadyLiked) {
                    repository.unlikePost(postId, currentUserId)
                } else {
                    repository.likePost(
                        CommunityLike(
                            postId = postId,
                            userId = currentUserId,
                            createdAt = getCurrentTimestamp()
                        )
                    )
                }

                refreshPostInteraction(postId)

            } catch (e: Exception) {
                showError("Unable to update like.")
            }
        }
    }

    // Load comments for a post
    fun loadComments(postId: Int) {
        viewModelScope.launch {
            repository.getCommentsByPost(postId).collect { comments ->
                _uiState.value = _uiState.value.copy(
                    comments = comments,
                    commentCount = comments.size
                )
            }
        }
    }

    // Add a comment
    fun addComment(
        postId: Int,
        content: String
    ) {
        viewModelScope.launch {
            val cleanContent = content.trim()

            if (cleanContent.isBlank()) {
                showError("Please enter a comment.")
                return@launch
            }

            try {
                repository.addComment(
                    CommunityComment(
                        postId = postId,
                        userId = currentUserId,
                        content = cleanContent,
                        createdAt = getCurrentTimestamp()
                    )
                )

                _uiState.value = _uiState.value.copy(
                    successMessage = "Comment added successfully."
                )

            } catch (e: Exception) {
                showError("Unable to add comment.")
            }
        }
    }

    // Refresh like status and count for a post
    private suspend fun refreshPostInteraction(postId: Int) {
        val liked = repository.hasUserLikedPost(
            postId,
            currentUserId
        )

        val count = repository.getLikeCount(postId).first()

        val currentLikedIds = _uiState.value.likedPostIds.toMutableSet()
        val currentCounts = _uiState.value.likeCounts.toMutableMap()

        if (liked) {
            currentLikedIds.add(postId)
        } else {
            currentLikedIds.remove(postId)
        }

        currentCounts[postId] = count

        _uiState.value = _uiState.value.copy(
            likedPostIds = currentLikedIds,
            likeCounts = currentCounts
        )
    }

    // Refresh interactions for loaded posts
    private suspend fun refreshPostInteractions(posts: List<CommunityPost>) {
        posts.forEach { post ->
            refreshPostInteraction(post.id)
        }
    }

    // Clear success and error messages
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = message,
            isLoading = false
        )
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())
    }
}
