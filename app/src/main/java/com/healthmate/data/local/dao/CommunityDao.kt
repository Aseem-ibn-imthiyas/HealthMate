package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.healthmate.data.local.entity.Community
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createCommunity(community: Community): Long

    @Query("""
        SELECT * FROM communities
        ORDER BY createdAt DESC
    """)
    fun getAllCommunities(): Flow<List<Community>>

    @Query("""
        SELECT * FROM communities
        WHERE id = :communityId
        LIMIT 1
    """)
    suspend fun getCommunityById(communityId: Int): Community?

    @Query("""
        SELECT * FROM communities
        WHERE createdBy = :userId
        ORDER BY createdAt DESC
    """)
    fun getCommunitiesByUser(userId: Int): Flow<List<Community>>

    @Update
    suspend fun updateCommunity(community: Community)

    @Delete
    suspend fun deleteCommunity(community: Community)
}