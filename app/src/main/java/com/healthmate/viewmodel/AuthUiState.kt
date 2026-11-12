
package com.healthmate.viewmodel

import com.healthmate.data.local.entity.User

data class AuthUiState(
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val isCheckingSession: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isResetEmailRegistered: Boolean? = null
)
