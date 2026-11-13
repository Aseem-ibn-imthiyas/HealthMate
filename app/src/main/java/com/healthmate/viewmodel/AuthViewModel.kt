
package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.data.local.dao.UserSessionDao
import com.healthmate.data.local.entity.User
import com.healthmate.data.local.entity.UserSession
import com.healthmate.data.repository.UserRepository
import com.healthmate.security.PasswordHasher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AuthViewModel(
    private val userRepository: UserRepository,
    private val userSessionDao: UserSessionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AuthUiState(isCheckingSession = true)
    )
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        restoreSession()
    }

    fun checkResetEmail(email: String) {
        viewModelScope.launch {
            val normalizedEmail = email.trim().lowercase()

            if (normalizedEmail.isBlank() ||
                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(normalizedEmail).matches()
            ) {
                _uiState.value = _uiState.value.copy(
                    isResetEmailRegistered = null,
                    errorMessage = "Please enter a valid email address."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isResetEmailRegistered = null,
                errorMessage = null,
                successMessage = null
            )

            try {
                val emailExists =
                    userRepository.isEmailRegistered(normalizedEmail)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isResetEmailRegistered = emailExists,
                    successMessage = if (emailExists) {
                        "Email found. You can reset your password."
                    } else {
                        null
                    },
                    errorMessage = if (emailExists) {
                        null
                    } else {
                        "This email is not registered."
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to check email. Please try again."
                )
            }
        }
    }


    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            if (newPassword.length < 8) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Password must contain at least 8 characters."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                val user = userRepository.getUserByEmail(email)

                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "User account not found."
                    )
                    return@launch
                }

                val (passwordHash, passwordSalt) =
                    PasswordHasher.hashPassword(newPassword)

                userRepository.updateUser(
                    user.copy(
                        passwordHash = passwordHash,
                        passwordSalt = passwordSalt
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isResetEmailRegistered = null,
                    successMessage = "Password reset successfully.",
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unable to reset password. Please try again."
                )
            }
        }
    }


    // REGISTER
    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                val normalizedEmail = email.trim().lowercase()

                if (fullName.isBlank() || normalizedEmail.isBlank()) {
                    showError("Please complete all required fields.")
                    return@launch
                }

                if (!android.util.Patterns.EMAIL_ADDRESS
                        .matcher(normalizedEmail).matches()
                ) {
                    showError("Please enter a valid email address.")
                    return@launch
                }

                if (password.length < 8) {
                    showError("Password must contain at least 8 characters.")
                    return@launch
                }

                val (hash, salt) = PasswordHasher.hashPassword(password)

                val user = User(
                    fullName = fullName.trim(),
                    email = normalizedEmail,
                    passwordHash = hash,
                    passwordSalt = salt,
                    createdAt = currentDateTime()
                )

                val result = userRepository.registerUser(user)

                if (result == -1L) {
                    showError("This email address is already registered.")
                    return@launch
                }

                _uiState.value = AuthUiState(
                    successMessage = "Account created successfully."
                )

            } catch (e: Exception) {
                showError("Unable to create your account. Please try again.")
            }
        }
    }

    // LOGIN
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                if (email.isBlank() || password.isBlank()) {
                    showError("Please enter your email and password.")
                    return@launch
                }

                val user = userRepository.getUserByEmail(email)

                if (user == null || !PasswordHasher.verifyPassword(
                        password,
                        user.passwordHash,
                        user.passwordSalt
                    )
                ) {
                    showError("Invalid email or password.")
                    return@launch
                }

                val newSession = UserSession(
                    userId = user.id,
                    createdAt = currentDateTime()
                )

                userSessionDao.saveSession(newSession)

                _uiState.value = AuthUiState(
                    currentUser = user,
                    currentSession = newSession,
                    isLoggedIn = true,
                    successMessage = "Login successful."
                )

            } catch (e: Exception) {
                showError("Unable to sign in. Please try again.")
            }
        }
    }

    // RESTORE LOGIN SESSION

    private fun restoreSession() {
        viewModelScope.launch {
            try {
                val session = userSessionDao.getActiveSession()

                if (session != null) {
                    val user = userRepository.getUserById(session.userId)

                    if (user != null) {
                        _uiState.value = AuthUiState(
                            currentUser = user,
                            currentSession = session,
                            isLoggedIn = true,
                            isCheckingSession = false
                        )
                    } else {
                        userSessionDao.clearSession()
                    }
                }

                if (_uiState.value.isCheckingSession) {
                    _uiState.value = _uiState.value.copy(
                        isCheckingSession = false
                    )
                }

            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    isCheckingSession = false,
                    errorMessage = "Unable to restore your login session."
                )
            }
        }
    }

    // UPDATE PROFILE
    fun updateProfile(
        fullName: String,
        email: String,
        profileImageUri: String?
    ) {
        viewModelScope.launch {

            val currentUser = _uiState.value.currentUser

            if (currentUser == null) {
                showError("Please sign in to update your profile.")
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                if (fullName.isBlank()) {
                    showError("Please enter your full name.")
                    return@launch
                }

                if (!android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email.trim()).matches()
                ) {
                    showError("Please enter a valid email address.")
                    return@launch
                }

                val updatedUser = userRepository.updateProfile(
                    userId = currentUser.id,
                    fullName = fullName,
                    email = email,
                    profileImageUri = profileImageUri
                )

                if (updatedUser == null) {
                    showError("This email address is already registered.")
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    currentUser = updatedUser,
                    isLoading = false,
                    successMessage = "Profile updated successfully."
                )

            } catch (e: Exception) {
                showError("Unable to update your profile. Please try again.")
            }
        }
    }

    // CHANGE PASSWORD
    fun changePassword(
        currentPassword: String,
        newPassword: String
    ) {
        viewModelScope.launch {

            val currentUser = _uiState.value.currentUser

            if (currentUser == null) {
                showError("Please sign in to change your password.")
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {
                // Verify current password
                val isCurrentPasswordValid =
                    PasswordHasher.verifyPassword(
                        currentPassword,
                        currentUser.passwordHash,
                        currentUser.passwordSalt
                    )

                if (!isCurrentPasswordValid) {
                    showError("Your current password is incorrect.")
                    return@launch
                }

                if (newPassword.length < 8) {
                    showError(
                        "New password must contain at least 8 characters."
                    )
                    return@launch
                }

                // Generate new password hash and salt
                val (newHash, newSalt) =
                    PasswordHasher.hashPassword(newPassword)

                // Update password in Room
                val updatedUser = currentUser.copy(
                    passwordHash = newHash,
                    passwordSalt = newSalt
                )

                userRepository.updateUser(updatedUser)

                _uiState.value = _uiState.value.copy(
                    currentUser = updatedUser,
                    isLoading = false,
                    successMessage = "Password updated successfully.",
                    errorMessage = null
                )

            } catch (e: Exception) {
                showError(
                    "Unable to update your password. Please try again."
                )
            }
        }
    }

    fun getCurrentSessionDateTime(): String? {
        return _uiState.value.currentUser?.let {
            // The actual session timestamp will be loaded from Room next.
            null
        }
    }

    // LOGOUT
    fun logoutUser() {
        viewModelScope.launch {
            try {
                userSessionDao.clearSession()
                _uiState.value = AuthUiState(
                    successMessage = "Logged out successfully."
                )
            } catch (e: Exception) {
                showError("Unable to log out. Please try again.")
            }
        }
    }

    // CLEAR MESSAGES
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }

    private fun currentDateTime(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())
    }
}
