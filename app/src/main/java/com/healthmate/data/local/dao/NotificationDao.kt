
package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    // CREATE - Insert notification
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(
        notification: NotificationEntity
    ): Long

    // READ - Get notifications for a user
    @Query(
        """
        SELECT * FROM notifications
        WHERE userId = :userId
        ORDER BY createdAt DESC
        """
    )
    fun getNotifications(
        userId: Int
    ): Flow<List<NotificationEntity>>

    // READ - Get unread notification count
    @Query(
        """
        SELECT COUNT(*) FROM notifications
        WHERE userId = :userId
        AND isRead = 0
        """
    )
    fun getUnreadCount(
        userId: Int
    ): Flow<Int>

    // UPDATE - Mark one notification as read
    @Query(
        """
        UPDATE notifications
        SET isRead = 1
        WHERE id = :notificationId
        AND userId = :userId
        """
    )
    suspend fun markAsRead(
        notificationId: Int,
        userId: Int
    )

    // UPDATE - Mark all notifications as read
    @Query(
        """
        UPDATE notifications
        SET isRead = 1
        WHERE userId = :userId
        """
    )
    suspend fun markAllAsRead(
        userId: Int
    )

    // DELETE - Delete one notification
    @Query(
        """
        DELETE FROM notifications
        WHERE id = :notificationId
        AND userId = :userId
        """
    )
    suspend fun deleteNotification(
        notificationId: Int,
        userId: Int
    )

    // DELETE - Delete all notifications for a user
    @Query(
        """
        DELETE FROM notifications
        WHERE userId = :userId
        """
    )
    suspend fun deleteAllNotifications(
        userId: Int
    )
}
