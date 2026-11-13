package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.healthmate.data.local.entity.CommunityPost
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityPostDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createPost(post: CommunityPost): Long

    @Query("""
        SELECT * FROM community_posts
        WHERE communityId = :communityId
        ORDER BY createdAt DESC
    """)
    fun getPostsByCommunity(
        communityId: Int
    ): Flow<List<CommunityPost>>

    @Query("""
        SELECT * FROM community_posts
        ORDER BY createdAt DESC
    """)
    fun getAllPosts(): Flow<List<CommunityPost>>

    @Query("""
        SELECT * FROM community_posts
        WHERE userId = :userId
        ORDER BY createdAt DESC
    """)
    fun getPostsByUser(
        userId: Int
    ): Flow<List<CommunityPost>>

    @Query("""
        SELECT * FROM community_posts
        WHERE id = :postId
        LIMIT 1
    """)
    suspend fun getPostById(postId: Int): CommunityPost?

    @Update
    suspend fun updatePost(post: CommunityPost)

    @Delete
    suspend fun deletePost(post: CommunityPost)
}