
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
import androidx.compose.material.icons.filled.Search
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.healthmate.R


private val BackgroundColor = Color(0xFFF7F8FF)
private val PrimaryBlue = Color(0xFF0052CC)
private val HeadingColor = Color(0xFF0B1F38)
private val SecondaryColor = Color(0xFF52627A)
private val CardBorder = Color(0xFFE6ECF5)

private data class CalendarDay(
    val day: String,
    val date: String,
    val hasRecord: Boolean
)

@Composable
fun HealthRecordsScreen(
    onSearchByDate: (String) -> Unit = {},
    searchResults: List<HealthRecordUiModel> = emptyList(),
    onClearSearch: () -> Unit = {},
    records: List<HealthRecordUiModel>,
    modifier: Modifier = Modifier,
    onAddRecordClick: () -> Unit = {},
    onEditRecordClick: (HealthRecordUiModel) -> Unit = {},
    onDeleteRecordClick: (HealthRecordUiModel) -> Unit = {},
    onRecordClick: (HealthRecordUiModel) -> Unit = {},
    onDateSelected: (String) -> Unit = {}

) {

    var isSearchActive by remember {
        mutableStateOf(false)
    }

    // Today's date in ISO format
    val dateFormatter = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    // Selected date defaults to today
    var selectedDate by remember {
        mutableStateOf(dateFormatter.format(Date()))
    }

    var searchDateInput by remember {
        mutableStateOf("")
    }

    // Generate the latest seven dates, including today
    val calendarDays = remember {
        val calendar = Calendar.getInstance()

        (6 downTo 0).map { offset ->

            val dayCalendar = calendar.clone() as Calendar

            dayCalendar.add(Calendar.DAY_OF_YEAR, -offset)

            dateFormatter.format(dayCalendar.time)
        }
    }

    // Dates containing saved records
    val availableRecordDates = remember(records) {
        records.map { it.dateIso }.toSet()
    }

    // Records displayed based on the active filter
    val selectedRecords = remember(
        records,
        searchResults,
        isSearchActive,
        selectedDate
    ) {
        if (isSearchActive) {
            searchResults
        } else {
            records.filter { it.dateIso == selectedDate }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    text = stringResource(R.string.health_records_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(R.string.health_records_subtitle),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    contentDescription = stringResource(R.string.health_records_add),
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        // Search by Date
        OutlinedTextField(
            value = searchDateInput,
            onValueChange = {
                searchDateInput = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp)
                .padding(top = 20.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.health_records_search_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (searchDateInput.isNotBlank()) {
                            val date = searchDateInput.trim()

                            selectedDate = date
                            isSearchActive = true

                            onSearchByDate(date)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.health_records_search_accessibility),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Dynamic Calendar Date Selector
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 34.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(calendarDays) { date ->

                val parsedDate = dateFormatter.parse(date) ?: Date()

                val calendarDay = CalendarDay(
                    day = SimpleDateFormat(
                        "EEE",
                        Locale.US
                    ).format(parsedDate),

                    date = SimpleDateFormat(
                        "d",
                        Locale.US
                    ).format(parsedDate),

                    hasRecord = date in availableRecordDates
                )

                DateSelectorItem(
                    day = calendarDay,
                    selected = selectedDate == date,
                    onClick = {
                        selectedDate = date
                        onDateSelected(date)
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
                text = if (isSearchActive) {
                    stringResource(R.string.health_records_search_results)
                } else {
                    stringResource(R.string.health_records_saved_records)
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(9.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        horizontal = 11.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = stringResource(
                        R.string.health_records_count,
                        selectedRecords.size
                    ),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isSearchActive) {
                TextButton(
                    onClick = onClearSearch
                ) {
                    Text(
                        text = stringResource(R.string.health_records_clear),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Selected Date Records / Empty State
        if (selectedRecords.isEmpty()) {

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
                    items = selectedRecords,
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
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val mainTextColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
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
                    color = when {
                        selected -> MaterialTheme.colorScheme.onPrimary
                        day.hasRecord -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outlineVariant
                    },
                    shape = CircleShape
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
            .background(MaterialTheme.colorScheme.surface)
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
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
            ) {
                Text(
                    text = record.dateLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.health_records_edit),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.health_records_delete),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                title = stringResource(R.string.health_records_steps),
                value = "%,d".format(record.steps),
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = Color(0xFF0755E9)
                    )
                },
                background = MaterialTheme.colorScheme.surfaceVariant,
                iconBackground = MaterialTheme.colorScheme.surface,
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            HealthMetricTile(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.health_records_calories),
                value = record.calories.toString(),
                icon = {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = Color(0xFFE53935)
                    )
                },
                background = MaterialTheme.colorScheme.surfaceVariant,
                iconBackground = MaterialTheme.colorScheme.surface,
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            HealthMetricTile(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.health_records_water),
                value = "%,d".format(record.water),
                icon = {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF365F9D)
                    )
                },
                background = MaterialTheme.colorScheme.surfaceVariant,
                iconBackground = MaterialTheme.colorScheme.surface,
                valueColor = MaterialTheme.colorScheme.onSurface
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = record.updatedAt,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    text = stringResource(R.string.health_records_view_details),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
            text = stringResource(R.string.health_records_empty_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = stringResource(R.string.health_records_empty_description),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}



