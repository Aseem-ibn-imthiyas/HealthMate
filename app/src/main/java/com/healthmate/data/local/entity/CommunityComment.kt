package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "community_comments",
    indices = [
        Index(value = ["postId"]),
        Index(value = ["userId"])
    ]
)
data class CommunityComment(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val postId: Int,

    val userId: Int,

    val content: String,

    val createdAt: String
)