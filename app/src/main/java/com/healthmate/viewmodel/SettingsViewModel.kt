
package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.data.local.entity.UserSettings
import com.healthmate.data.repository.UserSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val notificationsEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SettingsViewModel(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // LOAD ALL SETTINGS
    fun loadSettings(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val settings =
                    userSettingsRepository.getSettings(userId)

                _uiState.value = _uiState.value.copy(
                    notificationsEnabled =
                        settings?.notificationsEnabled ?: false,
                    darkModeEnabled =
                        settings?.darkModeEnabled ?: false,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to load settings."
                )
            }
        }
    }

    // UPDATE NOTIFICATION PREFERENCE
    fun updateNotifications(userId: Int, enabled: Boolean) {
        viewModelScope.launch {
            try {
                val currentSettings =
                    userSettingsRepository.getSettings(userId)

                userSettingsRepository.saveSettings(
                    UserSettings(
                        userId = userId,
                        notificationsEnabled = enabled,
                        darkModeEnabled =
                            currentSettings?.darkModeEnabled ?: false
                    )
                )

                _uiState.value = _uiState.value.copy(
                    notificationsEnabled = enabled,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Unable to save notification settings."
                )
            }
        }
    }

    // UPDATE DARK MODE PREFERENCE
    fun updateDarkMode(userId: Int, enabled: Boolean) {
        viewModelScope.launch {
            try {
                val currentSettings =
                    userSettingsRepository.getSettings(userId)

                userSettingsRepository.saveSettings(
                    UserSettings(
                        userId = userId,
                        notificationsEnabled =
                            currentSettings?.notificationsEnabled ?: false,
                        darkModeEnabled = enabled
                    )
                )

                _uiState.value = _uiState.value.copy(
                    darkModeEnabled = enabled,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Unable to save dark mode settings."
                )
            }
        }
    }
}
