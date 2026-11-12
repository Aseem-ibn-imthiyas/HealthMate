
package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.healthmate.data.local.dao.UserSessionDao
import com.healthmate.data.repository.UserRepository

class AuthViewModelFactory(
    private val userRepository: UserRepository,
    private val userSessionDao: UserSessionDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(
                userRepository,
                userSessionDao
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
