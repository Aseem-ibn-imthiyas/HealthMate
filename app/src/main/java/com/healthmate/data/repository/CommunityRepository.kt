package com.healthmate.data.repository

import com.healthmate.data.local.dao.CommunityDao
import com.healthmate.data.local.dao.CommunityMemberDao
import com.healthmate.data.local.dao.CommunityPostDao
import com.healthmate.data.local.dao.CommunityLikeDao
import com.healthmate.data.local.dao.CommunityCommentDao

import com.healthmate.data.local.entity.Community
import com.healthmate.data.local.entity.CommunityMember
import com.healthmate.data.local.entity.CommunityPost
import com.healthmate.data.local.entity.CommunityLike
import com.healthmate.data.local.entity.CommunityComment

import com.healthmate.data.local.dao.UserDao

class CommunityRepository(
    private val userDao: UserDao,
    private val communityDao: CommunityDao,
    private val communityMemberDao: CommunityMemberDao,
    private val communityPostDao: CommunityPostDao,
    private val communityLikeDao: CommunityLikeDao,
    private val communityCommentDao: CommunityCommentDao
) {

    suspend fun getUserById(userId: Int) =
        userDao.getUserById(userId)
    // Community operations
    suspend fun createCommunity(community: Community): Long =
        communityDao.createCommunity(community)

    fun getAllCommunities() =
        communityDao.getAllCommunities()

    suspend fun getCommunityById(communityId: Int) =
        communityDao.getCommunityById(communityId)

    fun getCommunitiesByUser(userId: Int) =
        communityDao.getCommunitiesByUser(userId)

    suspend fun updateCommunity(community: Community) =
        communityDao.updateCommunity(community)

    suspend fun deleteCommunity(community: Community) =
        communityDao.deleteCommunity(community)

    // Membership operations
    suspend fun joinCommunity(member: CommunityMember): Long =
        communityMemberDao.joinCommunity(member)

    suspend fun leaveCommunity(communityId: Int, userId: Int) =
        communityMemberDao.leaveCommunity(communityId, userId)

    suspend fun isMember(communityId: Int, userId: Int) =
        communityMemberDao.isMember(communityId, userId)

    fun getCommunityMembers(communityId: Int) =
        communityMemberDao.getCommunityMembers(communityId)

    fun getJoinedCommunities(userId: Int) =
        communityMemberDao.getJoinedCommunities(userId)

    fun getMemberCount(communityId: Int) =
        communityMemberDao.getMemberCount(communityId)

    // Post operations
    suspend fun createPost(post: CommunityPost): Long =
        communityPostDao.createPost(post)

    fun getPostsByCommunity(communityId: Int) =
        communityPostDao.getPostsByCommunity(communityId)

    fun getAllPosts() =
        communityPostDao.getAllPosts()

    fun getPostsByUser(userId: Int) =
        communityPostDao.getPostsByUser(userId)

    suspend fun getPostById(postId: Int) =
        communityPostDao.getPostById(postId)

    suspend fun updatePost(post: CommunityPost) =
        communityPostDao.updatePost(post)

    suspend fun deletePost(post: CommunityPost) =
        communityPostDao.deletePost(post)

    // Like operations
    suspend fun likePost(like: CommunityLike): Long =
        communityLikeDao.likePost(like)

    suspend fun unlikePost(postId: Int, userId: Int) =
        communityLikeDao.unlikePost(postId, userId)

    suspend fun hasUserLikedPost(postId: Int, userId: Int) =
        communityLikeDao.hasUserLikedPost(postId, userId)

    fun getLikeCount(postId: Int) =
        communityLikeDao.getLikeCount(postId)

    fun getPostLikes(postId: Int) =
        communityLikeDao.getPostLikes(postId)

    // Comment operations
    suspend fun addComment(comment: CommunityComment): Long =
        communityCommentDao.addComment(comment)

    fun getCommentsByPost(postId: Int) =
        communityCommentDao.getCommentsByPost(postId)

    fun getCommentsByUser(userId: Int) =
        communityCommentDao.getCommentsByUser(userId)

    fun getCommentCount(postId: Int) =
        communityCommentDao.getCommentCount(postId)

    suspend fun deleteComment(comment: CommunityComment) =
        communityCommentDao.deleteComment(comment)

    suspend fun deleteCommentById(commentId: Int) =
        communityCommentDao.deleteCommentById(commentId)
}