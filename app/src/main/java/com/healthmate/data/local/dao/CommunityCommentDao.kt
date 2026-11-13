package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.healthmate.data.local.entity.CommunityComment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityCommentDao {

    @Insert
    suspend fun addComment(comment: CommunityComment): Long

    @Query("""
        SELECT * FROM community_comments
        WHERE postId = :postId
        ORDER BY createdAt ASC
    """)
    fun getCommentsByPost(
        postId: Int
    ): Flow<List<CommunityComment>>

    @Query("""
        SELECT * FROM community_comments
        WHERE userId = :userId
        ORDER BY createdAt DESC
    """)
    fun getCommentsByUser(
        userId: Int
    ): Flow<List<CommunityComment>>

    @Query("""
        SELECT COUNT(*) FROM community_comments
        WHERE postId = :postId
    """)
    fun getCommentCount(postId: Int): Flow<Int>

    @Delete
    suspend fun deleteComment(comment: CommunityComment)

    @Query("""
        DELETE FROM community_comments
        WHERE id = :commentId
    """)
    suspend fun deleteCommentById(commentId: Int)
}