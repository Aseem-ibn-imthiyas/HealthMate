package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.UserSettings

@Dao
interface UserSettingsDao {

    @Query("SELECT * FROM user_settings WHERE userId = :userId LIMIT 1")
    suspend fun getSettings(userId: Int): UserSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: UserSettings)
}