
package com.healthmate.ui.dashboard

import com.healthmate.R
import com.healthmate.ui.GoalAnimation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.notifications.NotificationsBottomSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.healthmate.ui.notifications.NotificationsBottomSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import com.healthmate.data.heartrate.HeartRateViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.healthmate.ui.notifications.model.NotificationUiModel

private val BackgroundColor = Color(0xFFF7F8FF)
private val PrimaryBlue = Color(0xFF2563EB)
private val HeadingColor = Color(0xFF0B1F38)
private val SecondaryColor = Color(0xFF52627A)
private val CardBorder = Color(0xFFE2EAF7)



@Composable
fun DashboardScreen(
    userFullName: String = "",
    userProfileImageUri: String? = null,

    todaySteps: Int = 0,
    todayCalories: Int = 0,
    todayWater: Int = 0,
    weeklyRecordCount: Int = 0,

    notifications: List<NotificationUiModel> = emptyList(),
    unreadNotificationCount: Int = 0,
    onMarkNotificationAsRead: (Int) -> Unit = {},
    onMarkAllNotificationsAsRead: () -> Unit = {},
    onDeleteNotification: (Int) -> Unit = {},

    onNotificationClick: () -> Unit = {},
    onViewPlanClick: () -> Unit = {},
    onDetailsClick: () -> Unit = {},
    onWeeklyLogClick: () -> Unit = {}
) {

    var showNotifications by remember {
        mutableStateOf(false)
    }

    val heartRateViewModel: HeartRateViewModel = viewModel()
    val heartRate by heartRateViewModel.heartRate.collectAsState()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // User Header
            DashboardHeader(
                userFullName = userFullName,
                userProfileImageUri = userProfileImageUri,
                unreadNotificationCount = unreadNotificationCount,
                onNotificationClick = {
                    showNotifications = true
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Daily Goal Banner
            DailyGoalBanner(
                onViewPlanClick = onViewPlanClick
            )

            Spacer(modifier = Modifier.height(27.dp))

            // Health Summary Heading
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.dashboard_health_summary),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(
                    onClick = onDetailsClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_details),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Health Summary Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StepsCard(
                        steps = todaySteps,
                        modifier = Modifier.weight(1f)
                    )

                    WaterCard(
                        water = todayWater,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CaloriesCard(
                        calories = todayCalories,
                        modifier = Modifier.weight(1f)
                    )

                    HeartRateCard(
                        bpm = heartRate?.bpm,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekly Health Log
            WeeklyHealthLog(
                recordCount = weeklyRecordCount,
                onClick = onWeeklyLogClick
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showNotifications) {
        NotificationsBottomSheet(
            onDismiss = {
                showNotifications = false
            },
            notifications = notifications,
            onMarkAsRead = onMarkNotificationAsRead,
            onMarkAllAsRead = onMarkAllNotificationsAsRead,
            onDeleteNotification = onDeleteNotification,
            onNotificationClick = { notification ->

                // Notification navigation will be implemented later.

            }
        )
    }
}

@Composable
private fun DashboardHeader(
    userFullName: String,
    userProfileImageUri: String?,
    unreadNotificationCount: Int,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Profile Image
        Box {
            if (!userProfileImageUri.isNullOrBlank()) {
                AsyncImage(
                    model = userProfileImageUri,
                    contentDescription = stringResource(R.string.dashboard_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(
                        id = R.drawable.profile_avatar
                    ),
                    contentDescription = stringResource(R.string.dashboard_profile_picture),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd)
                    .background(
                        Color(0xFF8CD64F),
                        CircleShape
                    )
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
            )
        }

        Spacer(modifier = Modifier.width(9.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.dashboard_good_morning),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = userFullName.ifBlank { "HealthMate User" },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Notification Button
        Box {

            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(44.dp)
                    .shadow(
                        5.dp,
                        CircleShape,
                        ambientColor = Color(0xFFB7C9E8).copy(alpha = 0.15f)
                    )
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = stringResource(R.string.dashboard_notifications),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }

            if (unreadNotificationCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(17.dp)
                        .background(
                            Color(0xFFEF4444),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (unreadNotificationCount > 9) {
                            "9+"
                        } else {
                            unreadNotificationCount.toString()
                        },
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                        fontSize = 9.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyGoalBanner(
    onViewPlanClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(184.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2454CC),
                        Color(0xFF2865ED),
                        Color(0xFF4A8AF5)
                    )
                )
            )
            .padding(20.dp)
    ) {

        // Decorative circle
        Box(
            modifier = Modifier
                .size(105.dp)
                .offset(x = (-55).dp, y = (-55).dp)
                .background(
                    Color.White.copy(alpha = 0.08f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.65f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.18f)
                        )
                        .padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(
                                Color(0xFFFFD45C),
                                CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stringResource(R.string.dashboard_daily_goal),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.dashboard_keep_moving),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(R.string.dashboard_goal_description),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFFE8F0FF)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            OutlinedButton(
                onClick = onViewPlanClick,
                modifier = Modifier.height(32.dp),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.White.copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(
                    horizontal = 11.dp,
                    vertical = 0.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.dashboard_view_plan),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(5.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Animated Goal Illustration
        Box(
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(17.dp))
                .background(
                    Color.White.copy(alpha = 0.15f)
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.25f),
                    RoundedCornerShape(17.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            GoalAnimation(
                animationRes = R.raw.happy
            )
        }
    }
}

@Composable
private fun StepsCard(
    steps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(174.dp)
            .clip(RoundedCornerShape(21.dp))
            .background(PrimaryBlue)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        Color.White.copy(alpha = 0.20f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                    contentDescription = stringResource(R.string.dashboard_steps_accessibility),
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(37.dp)
                    .border(
                        3.dp,
                        Color.White,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${(steps * 100 / 10000).coerceAtMost(100)}%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column {
            Text(
                text = stringResource(R.string.dashboard_steps),
                fontSize = 11.sp,
                color = Color.White
            )

            Text(
                text = "%,d".format(steps),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = stringResource(R.string.dashboard_steps_goal),
                fontSize = 11.sp,
                color = Color(0xFFDCE8FF)
            )
        }
    }
}

@Composable
private fun WaterCard(
    water: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(174.dp)
            .clip(RoundedCornerShape(21.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(21.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = stringResource(R.string.dashboard_water_accessibility),
                    tint = Color(0xFF31B8EE),
                    modifier = Modifier.size(20.dp)
                )
            }

            SmallStatusTag(
                text = stringResource(
                    R.string.dashboard_goal_percentage,
                    (water * 100 / 2000).coerceAtMost(100)
                ),
                color = Color(0xFF24B9F2),
                background = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Column {
            LinearProgressIndicator(
                progress = { (water / 2000f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape),
                color = Color(0xFF31B8EE),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = stringResource(R.string.dashboard_water),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "%,d ml".format(water),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.dashboard_water_goal),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CaloriesCard(
    calories: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(174.dp)
            .clip(RoundedCornerShape(21.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(21.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = stringResource(R.string.dashboard_calories_accessibility),
                    tint = Color(0xFFFF7043),
                    modifier = Modifier.size(21.dp)
                )
            }

            SmallStatusTag(
                text = stringResource(R.string.dashboard_active),
                color = Color(0xFFFF7043),
                background = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Column {
            LinearProgressIndicator(
                progress = { (calories / 600f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(CircleShape),
                color = Color(0xFFFF7043),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = stringResource(R.string.dashboard_calories),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "$calories kcal",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.dashboard_daily_activity),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun HeartRateCard(
    bpm: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(174.dp)
            .clip(RoundedCornerShape(21.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(21.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.dashboard_heart_rate_accessibility),
                    tint = Color(0xFF8670E9),
                    modifier = Modifier.size(20.dp)
                )
            }

            SmallStatusTag(
                text = stringResource(R.string.dashboard_syncing),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                background = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Column {
            HeartRateWaveform()

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.dashboard_heart_rate),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = bpm?.let {
                    stringResource(R.string.dashboard_bpm, it)
                } ?: stringResource(R.string.dashboard_no_bpm_reading),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(
                    if (bpm != null) {
                        R.string.dashboard_simulated_reading
                    } else {
                        R.string.dashboard_no_reading
                    }
                ),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}



@Composable
private fun HeartRateWaveform(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
    ) {
        val path = androidx.compose.ui.graphics.Path()

        val points = listOf(
            0.00f to 0.55f,
            0.12f to 0.55f,
            0.19f to 0.48f,
            0.25f to 0.62f,
            0.32f to 0.55f,
            0.40f to 0.55f,
            0.45f to 0.12f,
            0.50f to 0.95f,
            0.56f to 0.30f,
            0.63f to 0.55f,
            0.78f to 0.55f,
            0.86f to 0.48f,
            0.93f to 0.55f,
            1.00f to 0.55f
        )

        points.forEachIndexed { index, point ->
            val x = point.first * size.width
            val y = point.second * size.height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF8670E9),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 2.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round,
                join = androidx.compose.ui.graphics.StrokeJoin.Round
            )
        )
    }
}


@Composable
private fun SmallStatusTag(
    text: String,
    color: Color,
    background: Color
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .padding(
                horizontal = 9.dp,
                vertical = 5.dp
            )
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
private fun WeeklyHealthLog(
    recordCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = stringResource(
                    R.string.dashboard_weekly_log_accessibility
                ),
                tint = PrimaryBlue,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.dashboard_weekly_health_log),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(
                    R.string.dashboard_weekly_records,
                    recordCount
                ),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        TextButton(
            onClick = onClick,
            modifier = Modifier
                .height(36.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                ),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_view),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
