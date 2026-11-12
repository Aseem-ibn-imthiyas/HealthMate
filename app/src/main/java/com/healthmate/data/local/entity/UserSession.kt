
package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_sessions")
data class UserSession(
    @PrimaryKey
    val sessionId: Int = 1,
    val userId: Int,
    val createdAt: String
)
