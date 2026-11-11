package com.healthmate.ui.profile.model

data class SettingsUiModel(
    val fullName: String,
    val email: String,
    val profileImageRes: Int,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false,
    val selectedLanguage: String = "English"
)