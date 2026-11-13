package com.healthmate.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.healthmate.data.repository.HealthRecordRepository
import com.healthmate.data.repository.NotificationRepository

class HealthRecordViewModelFactory(
    private val repository: HealthRecordRepository,
    private val userId: Int,
    private val notificationRepository: NotificationRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                HealthRecordViewModel::class.java
            )
        ) {
            return HealthRecordViewModel(
                repository = repository,
                userId = userId,
                notificationRepository = notificationRepository,
                context = context
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}