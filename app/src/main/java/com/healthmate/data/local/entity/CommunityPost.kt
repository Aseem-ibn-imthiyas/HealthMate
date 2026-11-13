package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_posts",
    indices = [
        Index(value = ["communityId"]),
        Index(value = ["userId"])
    ]
)
data class CommunityPost(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val communityId: Int,

    val userId: Int,

    val content: String,

    val createdAt: String,

    val imageUri: String? = null
)