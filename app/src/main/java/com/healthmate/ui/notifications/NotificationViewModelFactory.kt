package com.healthmate.ui.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.healthmate.data.repository.NotificationRepository
import com.healthmate.viewmodel.NotificationViewModel

class NotificationViewModelFactory(
    private val repository: NotificationRepository,
    private val userId: Int,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(
                repository = repository,
                userId = userId,
                context = context.applicationContext
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}