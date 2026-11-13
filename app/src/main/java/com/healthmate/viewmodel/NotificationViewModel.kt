package com.healthmate.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmate.data.repository.NotificationRepository
import com.healthmate.notifications.HealthMateNotificationManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class NotificationViewModel(
    private val repository: NotificationRepository,
    private val userId: Int,
    private val context: Context

) : ViewModel() {

    val notifications = repository
        .getNotifications(userId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val unreadCount = repository
        .getUnreadCount(userId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    fun createNotification(
        title: String,
        message: String,
        type: String
    ) {
        viewModelScope.launch {
            try {
                Log.d("HealthMateVM", "Saving notification to Room...")

                val notificationId = repository.createNotification(
                    userId = userId,
                    title = title,
                    message = message,
                    type = type
                )

                Log.d(
                    "HealthMateVM",
                    "Room saved notification ID: $notificationId"
                )

                Log.d("HealthMateVM", "Calling Android notification manager...")

                HealthMateNotificationManager.showNotification(
                    context = context,
                    notificationId = notificationId.toInt(),
                    title = title,
                    message = message
                )

                Log.d("HealthMateVM", "Notification manager call completed.")

            } catch (exception: Exception) {
                Log.e(
                    "HealthMateVM",
                    "Notification creation failed",
                    exception
                )
            }
        }
    }
    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            repository.markAsRead(notificationId, userId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead(userId)
        }
    }

    fun deleteNotification(notificationId: Int) {
        viewModelScope.launch {
            repository.deleteNotification(notificationId, userId)
        }
    }

    fun deleteAllNotifications() {
        viewModelScope.launch {
            repository.deleteAllNotifications(userId)
        }
    }
}