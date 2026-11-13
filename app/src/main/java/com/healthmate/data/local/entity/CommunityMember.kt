package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_members",
    indices = [
        Index(
            value = ["communityId", "userId"],
            unique = true
        ),
        Index(value = ["userId"])
    ]
)
data class CommunityMember(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val communityId: Int,

    val userId: Int,

    val joinedAt: String
)