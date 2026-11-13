package com.healthmate.data.repository

import com.healthmate.data.local.dao.NotificationDao
import com.healthmate.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val notificationDao: NotificationDao
) {

    fun getNotifications(userId: Int): Flow<List<NotificationEntity>> {
        return notificationDao.getNotifications(userId)
    }

    fun getUnreadCount(userId: Int): Flow<Int> {
        return notificationDao.getUnreadCount(userId)
    }

    suspend fun createNotification(
        userId: Int,
        title: String,
        message: String,
        type: String
    ): Long {
        val notification = NotificationEntity(
            userId = userId,
            title = title,
            message = message,
            type = type,
            createdAt = System.currentTimeMillis(),
            isRead = false
        )

        return notificationDao.insertNotification(notification)
    }

    suspend fun markAsRead(notificationId: Int, userId: Int) {
        notificationDao.markAsRead(notificationId, userId)
    }

    suspend fun markAllAsRead(userId: Int) {
        notificationDao.markAllAsRead(userId)
    }

    suspend fun deleteNotification(notificationId: Int, userId: Int) {
        notificationDao.deleteNotification(notificationId, userId)
    }

    suspend fun deleteAllNotifications(userId: Int) {
        notificationDao.deleteAllNotifications(userId)
    }
}