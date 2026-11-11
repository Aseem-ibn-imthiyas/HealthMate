
package com.healthmate.ui.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.healthrecords.model.HealthRecordUiModel

private val BackgroundColor = Color(0xFFF7F8FF)
private val PrimaryBlue = Color(0xFF0052CC)
private val HeadingColor = Color(0xFF0B1F38)
private val SecondaryColor = Color(0xFF52627A)
private val CardBorder = Color(0xFFE6ECF5)

private data class CalendarDay(
    val day: String,
    val date: String,
    val selected: Boolean = false
)

private val calendarDays = listOf(
    CalendarDay("Thu", "19"),
    CalendarDay("Fri", "20"),
    CalendarDay("Sat", "21"),
    CalendarDay("Sun", "22"),
    CalendarDay("Mon", "23"),
    CalendarDay("Tue", "24", true),
    CalendarDay("Wed", "25")
)

private val sampleRecords = listOf(
    HealthRecordUiModel(
        id = 1,
        date = "Oct 24",
        dateLabel = "Today",
        steps = 7850,
        calories = 420,
        water = 2000,
        updatedAt = "Updated 20 mins ago"
    ),
    HealthRecordUiModel(
        id = 2,
        date = "Oct 23",
        dateLabel = "Yesterday",
        steps = 9120,
        calories = 510,
        water = 2250,
        updatedAt = "Updated yesterday at 9:45 PM"
    ),
    HealthRecordUiModel(
        id = 3,
        date = "Tue, Oct 22",
        dateLabel = "Logged",
        steps = 6500,
        calories = 380,
        water = 1400,
        updatedAt = "Updated Oct 22 at 8:15 PM"
    )
)

@Composable
fun HealthRecordsScreen(
    modifier: Modifier = Modifier,
    onAddRecordClick: () -> Unit = {},
    onEditRecordClick: (HealthRecordUiModel) -> Unit = {},
    onDeleteRecordClick: (HealthRecordUiModel) -> Unit = {},
    onRecordClick: (HealthRecordUiModel) -> Unit = {},
    onDateSelected: (String) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf("24") }
    var showEmptyDemo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 27.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Health Records",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeadingColor
                )

                Text(
                    text = "Daily vitals & activity telemetry",
                    fontSize = 11.sp,
                    color = SecondaryColor
                )
            }

            IconButton(
                onClick = onAddRecordClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(PrimaryBlue, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new record",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        // Calendar Date Selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 34.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(calendarDays) { day ->
                DateSelectorItem(
                    day = day,
                    selected = selectedDate == day.date,
                    onClick = {
                        selectedDate = day.date
                        onDateSelected(day.date)
                    }
                )
            }
        }

        // Saved Records Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 35.dp,
                    bottom = 22.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Records",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = HeadingColor
            )

            Spacer(modifier = Modifier.width(9.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFDCE9FA))
                    .padding(
                        horizontal = 11.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = if (showEmptyDemo) "0 records" else "3 records",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF29466D)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (showEmptyDemo) "Show Records" else "Show Empty Demo",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryBlue,
                modifier = Modifier.clickable {
                    showEmptyDemo = !showEmptyDemo
                }
            )
        }

        // Records / Empty State
        if (showEmptyDemo) {
            EmptyRecordsState(
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 18.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = sampleRecords,
                    key = { it.id }
                ) { record ->
                    HealthRecordCard(
                        record = record,
                        onEditClick = {
                            onEditRecordClick(record)
                        },
                        onDeleteClick = {
                            onDeleteRecordClick(record)
                        },
                        onClick = {
                            onRecordClick(record)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSelectorItem(
    day: CalendarDay,
    selected: Boolean,
    onClick: () -> Unit
) {
    val itemBackground = if (selected) {
        PrimaryBlue
    } else {
        Color.White
    }

    val mainTextColor = if (selected) {
        Color.White
    } else {
        Color(0xFF52627A)
    }

    Column(
        modifier = Modifier
            .width(43.dp)
            .height(65.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(itemBackground)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day.day,
            fontSize = 10.sp,
            color = mainTextColor
        )

        Text(
            text = day.date,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = mainTextColor
        )

        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    if (selected) Color.White else Color(0xFFD5E3F8),
                    CircleShape
                )
        )
    }
}

@Composable
private fun HealthRecordCard(
    record: HealthRecordUiModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(29.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {

        // Record Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = record.date,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = HeadingColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFE0E8FF))
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = record.dateLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF234B9A)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit record",
                    tint = SecondaryColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete record",
                    tint = SecondaryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Health Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HealthMetricTile(
                modifier = Modifier.weight(1f),
                title = "Steps",
                value = "%,d".format(record.steps),
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = Color(0xFF0755E9)
                    )
                },
                background = Color(0xFFEDF3FF),
                iconBackground = Color(0xFFDCE6FF),
                valueColor = Color(0xFF0755E9)
            )

            HealthMetricTile(
                modifier = Modifier.weight(1f),
                title = "kcal",
                value = record.calories.toString(),
                icon = {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFE53935)
                    )
                },
                background = Color(0xFFFFEEEE),
                iconBackground = Color(0xFFFFDCDC),
                valueColor = Color(0xFFB71C1C)
            )

            HealthMetricTile(
                modifier = Modifier.weight(1f),
                title = "ml",
                value = "%,d".format(record.water),
                icon = {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF365F9D)
                    )
                },
                background = Color(0xFFEAF2FC),
                iconBackground = Color(0xFFD7E5F9),
                valueColor = Color(0xFF365F9D)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Record Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = SecondaryColor,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = record.updatedAt,
                fontSize = 10.sp,
                color = SecondaryColor,
                modifier = Modifier.weight(1f)
            )

            // View Details Button
            TextButton(
                onClick = onClick,
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 2.dp
                )
            ) {
                Text(
                    text = "View Details",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlue
                )
            }
        }

    }
}

@Composable
private fun HealthMetricTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    background: Color,
    iconBackground: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(background)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    iconBackground,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(17.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }

        Column {
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                maxLines = 1
            )

            Text(
                text = title,
                fontSize = 10.sp,
                color = SecondaryColor
            )
        }
    }
}

@Composable
private fun EmptyRecordsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = Color(0xFF8BA9D8),
            modifier = Modifier.size(54.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "No Health Records Yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HeadingColor
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = "Start tracking your daily activities by adding your first health record.",
            fontSize = 13.sp,
            color = SecondaryColor,
            lineHeight = 20.sp
        )
    }
}



