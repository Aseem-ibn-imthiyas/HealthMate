package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_likes",
    indices = [
        Index(
            value = ["postId", "userId"],
            unique = true
        ),
        Index(value = ["userId"])
    ]
)
data class CommunityLike(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val postId: Int,

    val userId: Int,

    val createdAt: String
)