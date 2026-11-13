
package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["userId", "createdAt"])
    ]
)
data class NotificationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val userId: Int,

    val title: String,

    val message: String,

    val type: String,

    val createdAt: Long,

    val isRead: Boolean = false
)
