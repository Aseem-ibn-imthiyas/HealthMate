package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(

    @PrimaryKey
    val userId: Int,

    val notificationsEnabled: Boolean = false,

    val darkModeEnabled: Boolean = false
)