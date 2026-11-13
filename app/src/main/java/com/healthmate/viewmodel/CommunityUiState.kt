package com.healthmate.viewmodel

import com.healthmate.data.local.entity.Community
import com.healthmate.data.local.entity.CommunityPost
import com.healthmate.data.local.entity.CommunityComment
import com.healthmate.ui.communities.model.CommunityPostUiModel

data class CommunityUiState(

    // Community data
    val communities: List<Community> = emptyList(),
    val selectedCommunity: Community? = null,
    val joinedCommunityIds: Set<Int> = emptySet(),
    val memberCount: Int = 0,

    // Post data
    val posts: List<CommunityPost> = emptyList(),

    // Like data
    val likedPostIds: Set<Int> = emptySet(),
    val likeCounts: Map<Int, Int> = emptyMap(),

    // Comment data
    val comments: List<CommunityComment> = emptyList(),
    val commentCount: Int = 0,

    // UI feedback
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val postUiModels: List<CommunityPostUiModel> = emptyList()
)