package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.CommunityLike
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityLikeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun likePost(like: CommunityLike): Long

    @Query("""
        DELETE FROM community_likes
        WHERE postId = :postId
        AND userId = :userId
    """)
    suspend fun unlikePost(
        postId: Int,
        userId: Int
    )

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM community_likes
            WHERE postId = :postId
            AND userId = :userId
        )
    """)
    suspend fun hasUserLikedPost(
        postId: Int,
        userId: Int
    ): Boolean

    @Query("""
        SELECT COUNT(*) FROM community_likes
        WHERE postId = :postId
    """)
    fun getLikeCount(postId: Int): Flow<Int>

    @Query("""
        SELECT * FROM community_likes
        WHERE postId = :postId
        ORDER BY createdAt DESC
    """)
    fun getPostLikes(
        postId: Int
    ): Flow<List<CommunityLike>>
}