
package com.healthmate.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.notifications.model.NotificationType
import com.healthmate.ui.notifications.model.NotificationUiModel

private val NotificationBlue = Color(0xFF0752CC)
private val NotificationDark = Color(0xFF102641)
private val NotificationSecondary = Color(0xFF53647C)
private val NotificationPaleBlue = Color(0xFFEEF3FF)
private val NotificationIconBlue = Color(0xFFDCE8FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    onDismiss: () -> Unit,
    onNotificationClick: (NotificationUiModel) -> Unit = {}
) {

    var notifications by remember {
        mutableStateOf(
            listOf(
                NotificationUiModel(
                    id = 1,
                    title = "Daily Goal Reminder",
                    message = "You're getting closer to your daily step goal. Keep moving!",
                    time = "10 min ago",
                    type = NotificationType.GOAL,
                    isRead = false
                ),
                NotificationUiModel(
                    id = 2,
                    title = "Stay Hydrated",
                    message = "Time to drink some water! Keep your hydration goal on track.",
                    time = "1 hour ago",
                    type = NotificationType.WATER,
                    isRead = false
                ),
                NotificationUiModel(
                    id = 3,
                    title = "Community Update",
                    message = "New activities and discussions are waiting in your walking squad.",
                    time = "3 hours ago",
                    type = NotificationType.COMMUNITY,
                    isRead = true
                ),
                NotificationUiModel(
                    id = 4,
                    title = "Record Saved Successfully",
                    message = "Your daily biometric and blood pressure logs have been synchronized.",
                    time = "Yesterday",
                    type = NotificationType.RECORD,
                    isRead = true
                ),
                NotificationUiModel(
                    id = 5,
                    title = "Weekly Health Summary",
                    message = "Your weekly health progress summary is ready to review.",
                    time = "Yesterday",
                    type = NotificationType.SUMMARY,
                    isRead = true
                )
            )
        )
    }

    val unreadCount = notifications.count { !it.isRead }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = NotificationDark,
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp
        ),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 40.dp,
                height = 4.dp,
                color = Color(0xFFD4DCE9)
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.15f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 10.dp,
                        bottom = 17.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Notifications",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = NotificationDark
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "Stay updated on your health journey.",
                        fontSize = 12.sp,
                        color = NotificationSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(37.dp)
                        .background(
                            NotificationPaleBlue,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close notifications",
                        tint = NotificationSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE5EBF5),
                thickness = 1.dp
            )

            // Recent and Mark All Read
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Recent",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NotificationDark
                )

                if (unreadCount > 0) {
                    Spacer(Modifier.width(9.dp))

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFDCE8FA))
                            .padding(
                                horizontal = 11.dp,
                                vertical = 4.dp
                            )
                    ) {
                        Text(
                            text = "$unreadCount New",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = NotificationDark
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.clickable {
                        notifications = notifications.map {
                            it.copy(isRead = true)
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = NotificationBlue,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = "Mark All Read",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = NotificationBlue
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFFE9EDF5),
                thickness = 1.dp
            )

            // Notification List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 13.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = notifications,
                    key = { it.id }
                ) { notification ->

                    NotificationItem(
                        notification = notification,

                        onClick = {

                            notifications = notifications.map {
                                if (it.id == notification.id) {
                                    it.copy(isRead = true)
                                } else {
                                    it
                                }
                            }

                            onNotificationClick(notification)
                        },

                        onDelete = {
                            notifications = notifications.filterNot {
                                it.id == notification.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: NotificationUiModel,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    val (icon, iconColor, iconBackground) =
        notificationIcon(notification.type)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(
                if (!notification.isRead) {
                    NotificationPaleBlue
                } else {
                    Color.White
                }
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = 14.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.Top
    ) {

        // Notification Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    iconBackground,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        // Notification Details
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = notification.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = NotificationDark
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = notification.message,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = NotificationSecondary
            )

            Spacer(Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color(0xFF758198),
                    modifier = Modifier.size(12.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = notification.time,
                    fontSize = 10.sp,
                    color = Color(0xFF758198)
                )
            }
        }

        // Unread Indicator
        // Delete Button and Unread Indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete notification",
                    tint = Color(0xFF8795AA),
                    modifier = Modifier.size(19.dp)
                )
            }

            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .size(7.dp)
                        .background(
                            NotificationBlue,
                            CircleShape
                        )
                )
            }
        }
    }
}

private fun notificationIcon(
    type: NotificationType
): Triple<ImageVector, Color, Color> {

    return when (type) {

        NotificationType.GOAL -> Triple(
            Icons.Default.DirectionsWalk,
            Color(0xFF0752CC),
            Color(0xFFDCE5FF)
        )

        NotificationType.WATER -> Triple(
            Icons.Default.WaterDrop,
            Color(0xFF1768E8),
            Color(0xFFD7E7FF)
        )

        NotificationType.COMMUNITY -> Triple(
            Icons.Default.Groups,
            Color(0xFF31558F),
            Color(0xFFDCE8FA)
        )

        NotificationType.RECORD -> Triple(
            Icons.Default.AssignmentTurnedIn,
            Color(0xFF455A70),
            Color(0xFFDCE8FA)
        )

        NotificationType.SUMMARY -> Triple(
            Icons.Default.TrendingUp,
            Color(0xFF0752CC),
            Color(0xFFDCE8FA)
        )
    }
}
