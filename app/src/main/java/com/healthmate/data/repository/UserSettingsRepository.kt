package com.healthmate.data.repository

import com.healthmate.data.local.dao.UserSettingsDao
import com.healthmate.data.local.entity.UserSettings

class UserSettingsRepository(
    private val userSettingsDao: UserSettingsDao
) {

    suspend fun getSettings(userId: Int): UserSettings? {
        return userSettingsDao.getSettings(userId)
    }

    suspend fun saveSettings(settings: UserSettings) {
        userSettingsDao.saveSettings(settings)
    }
}