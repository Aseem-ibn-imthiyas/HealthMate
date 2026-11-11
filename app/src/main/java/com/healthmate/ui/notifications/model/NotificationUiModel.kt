
package com.healthmate.ui.notifications.model

enum class NotificationType {
    GOAL,
    WATER,
    COMMUNITY,
    RECORD,
    SUMMARY
}

data class NotificationUiModel(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean
)
