
package com.healthmate.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.healthmate.MainActivity
import com.healthmate.R
import android.util.Log

object HealthMateNotificationManager {

    private const val CHANNEL_ID = "healthmate_notifications"
    private const val CHANNEL_NAME = "HealthMate Notifications"

    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Health and wellness reminders and updates"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String
    ) {

        Log.d("HealthMateNotify", "showNotification() called: $title")

        // Ensure notification channel exists before displaying notification
        createNotificationChannel(context)

        // Check Android 13+ notification permission
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("HealthMateNotify", "Notification permission NOT granted")
            return
        }

        Log.d("HealthMateNotify", "Notification permission granted")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        Log.d("HealthMateNotify", "Posting notification ID: $notificationId")

        NotificationManagerCompat
            .from(context)
            .notify(notificationId, notification)
    }
}
