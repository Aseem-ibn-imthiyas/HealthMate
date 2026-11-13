package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.CommunityMember
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityMemberDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun joinCommunity(member: CommunityMember): Long

    @Query("""
        DELETE FROM community_members
        WHERE communityId = :communityId
        AND userId = :userId
    """)
    suspend fun leaveCommunity(
        communityId: Int,
        userId: Int
    )

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM community_members
            WHERE communityId = :communityId
            AND userId = :userId
        )
    """)
    suspend fun isMember(
        communityId: Int,
        userId: Int
    ): Boolean

    @Query("""
        SELECT * FROM community_members
        WHERE communityId = :communityId
        ORDER BY joinedAt DESC
    """)
    fun getCommunityMembers(
        communityId: Int
    ): Flow<List<CommunityMember>>

    @Query("""
        SELECT * FROM community_members
        WHERE userId = :userId
        ORDER BY joinedAt DESC
    """)
    fun getJoinedCommunities(
        userId: Int
    ): Flow<List<CommunityMember>>

    @Query("""
        SELECT COUNT(*) FROM community_members
        WHERE communityId = :communityId
    """)
    fun getMemberCount(
        communityId: Int
    ): Flow<Int>
}