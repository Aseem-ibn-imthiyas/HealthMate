
package com.healthmate.ui.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.healthrecords.model.HealthRecordUiModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val DetailsBackground = Color(0xFFF7F8FF)
private val DetailsHeading = Color(0xFF102641)
private val DetailsSecondary = Color(0xFF59677D)
private val DetailsBlue = Color(0xFF0755D9)
private val DetailsBorder = Color(0xFFEAF0F8)

@Composable
fun ViewHealthRecordDetailsScreen(
    record: HealthRecordUiModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSynced: Boolean = true,
    stepsTarget: Int = 10000,
    waterTarget: Int = 2500,
    deviceSource: String = "Pixel 7 Sensors"
) {

    // Convert the stored ISO date into display formats
    val parsedDate = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.US
    ).parse(record.dateIso)

    val displayDate = parsedDate?.let {
        SimpleDateFormat(
            "EEEE, MMMM dd",
            Locale.US
        ).format(it)
    } ?: record.date

    val todayDate = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.US
    ).format(Date())

    val yesterdayCalendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }

    val yesterdayDate = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.US
    ).format(yesterdayCalendar.time)

    val dateLabel = when (record.dateIso) {
        todayDate -> "Today"
        yesterdayDate -> "Yesterday"
        else -> "Logged"
    }

    val dateBadge = "${record.date} • $dateLabel"

    val stepsProgress = if (stepsTarget > 0) {
        (record.steps.toFloat() / stepsTarget).coerceIn(0f, 1f)
    } else 0f

    val waterProgress = if (waterTarget > 0) {
        (record.water.toFloat() / waterTarget).coerceIn(0f, 1f)
    } else 0f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DetailsBackground)
            .statusBarsPadding()
    ) {

        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DetailsHeading,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Record Details",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = DetailsHeading
            )
        }

        // Scrollable Details Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Date Summary Card
            DateSummaryCard(
                dateBadge = dateBadge,
                displayDate = displayDate,
                isSynced = isSynced
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Daily Overview
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Overview",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DetailsHeading
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "3 Recorded",
                    fontSize = 12.sp,
                    color = DetailsBlue
                )
            }

            DailyMetricCard(
                title = "Steps Walked",
                value = "%,d".format(record.steps),
                unit = "steps",
                subtitle = "Daily step activity",
                icon = {
                    Icon(
                        Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        tint = DetailsBlue,
                        modifier = Modifier.size(23.dp)
                    )
                },
                iconBackground = Color(0xFFE5EEFF),
                valueColor = DetailsHeading,
                chartColor = DetailsBlue
            )

            Spacer(modifier = Modifier.height(9.dp))

            DailyMetricCard(
                title = "Calories Burned",
                value = record.calories.toString(),
                unit = "kcal",
                subtitle = "Daily energy expenditure",
                icon = {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFD52222),
                        modifier = Modifier.size(23.dp)
                    )
                },
                iconBackground = Color(0xFFFFEEEE),
                valueColor = DetailsHeading,
                chartColor = Color(0xFFE52B2B)
            )

            Spacer(modifier = Modifier.height(9.dp))

            DailyMetricCard(
                title = "Water Intake",
                value = "%,d".format(record.water),
                unit = "ml",
                subtitle = "Daily hydration",
                icon = {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF365D9B),
                        modifier = Modifier.size(23.dp)
                    )
                },
                iconBackground = Color(0xFFDCE9FB),
                valueColor = DetailsHeading,
                chartColor = Color(0xFF365D9B)
            )

            Spacer(modifier = Modifier.height(31.dp))

            // Daily Goal Progress
            Text(
                text = "Daily Goal Progress",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsHeading
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {

                GoalProgressItem(
                    title = "Steps Goal",
                    value = stepsProgress,
                    percentage = (stepsProgress * 100).toInt(),
                    completedText = "%,d completed".format(record.steps),
                    targetText = "%,d target".format(stepsTarget),
                    color = DetailsBlue,
                    icon = {
                        Icon(
                            Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = DetailsBlue,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(17.dp))

                GoalProgressItem(
                    title = "Water Goal",
                    value = waterProgress,
                    percentage = (waterProgress * 100).toInt(),
                    completedText = "%,d ml drank".format(record.water),
                    targetText = "%,d ml target".format(waterTarget),
                    color = Color(0xFF5274A9),
                    icon = {
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color(0xFF365D9B),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calories Summary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFEEF3FF))
                        .padding(
                            horizontal = 10.dp,
                            vertical = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Color(0xFFFFE7E7),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color(0xFFE52222),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Calories Summary",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DetailsHeading
                        )

                        Text(
                            text = "Recorded energy burn",
                            fontSize = 10.sp,
                            color = DetailsSecondary
                        )
                    }

                    Text(
                        text = "${record.calories} kcal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD52222)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Record Information
            Text(
                text = "Record Information",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = DetailsHeading
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {

                InformationRow(
                    label = "Record Date",
                    value = displayDate
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Record Status",
                        fontSize = 13.sp,
                        color = DetailsSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFE7F0FF))
                            .padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = DetailsBlue,
                            modifier = Modifier.size(12.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Saved",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = DetailsBlue
                        )
                    }
                }

                InformationRow(
                    label = "Record Type",
                    value = "Daily Health Record"
                )

                InformationRow(
                    label = "Device Source",
                    value = deviceSource
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DateSummaryCard(
    dateBadge: String,
    displayDate: String,
    isSynced: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFE7F0FF))
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    )
            ) {
                Text(
                    text = "● $dateBadge",
                    fontSize = 10.sp,
                    color = DetailsBlue,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = null,
                tint = if (isSynced) DetailsBlue else DetailsSecondary,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = if (isSynced) "Synced" else "Not Synced",
                fontSize = 10.sp,
                color = DetailsSecondary
            )
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = displayDate,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DetailsHeading,
            lineHeight = 29.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Your daily health activity summary",
            fontSize = 13.sp,
            color = DetailsSecondary
        )
    }
}

@Composable
private fun DailyMetricCard(
    title: String,
    value: String,
    unit: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    iconBackground: Color,
    valueColor: Color,
    chartColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(91.dp)
            .clip(RoundedCornerShape(29.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    iconBackground,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = DetailsHeading
            )

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = unit,
                    fontSize = 11.sp,
                    color = DetailsHeading,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = DetailsSecondary
            )
        }

        MiniBarChart(color = chartColor)
    }
}

@Composable
private fun MiniBarChart(
    color: Color
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFF0F5FF))
            .padding(horizontal = 11.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color.copy(alpha = 0.30f))
        )

        Box(
            modifier = Modifier
                .width(6.dp)
                .height(21.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color.copy(alpha = 0.55f))
        )

        Box(
            modifier = Modifier
                .width(6.dp)
                .height(27.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
    }
}

@Composable
private fun GoalProgressItem(
    title: String,
    value: Float,
    percentage: Int,
    completedText: String,
    targetText: String,
    color: Color,
    icon: @Composable () -> Unit
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            icon()

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DetailsHeading,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f))
                    .padding(
                        horizontal = 9.dp,
                        vertical = 3.dp
                    )
            ) {
                Text(
                    text = "%.1f%%".format(
                        value * 100
                    ),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Custom progress indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(CircleShape)
                .background(Color(0xFFE4EDFA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(color)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = completedText,
                fontSize = 10.sp,
                color = DetailsSecondary
            )

            Text(
                text = targetText,
                fontSize = 10.sp,
                color = DetailsSecondary
            )
        }
    }
}

@Composable
private fun InformationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = DetailsSecondary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = DetailsHeading
        )
    }
}
