package com.healthmate.ui.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FormBackground = Color(0xFFF7F8FC)
private val FormPrimary = Color(0xFF4035AC)
private val FormHeading = Color(0xFF17243B)
private val FormSecondary = Color(0xFF64748B)
private val FormBorder = Color(0xFFDCE4F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthRecordScreen(
    onDismiss: () -> Unit,
    onSaveRecord: (
        date: String,
        steps: Int,
        calories: Int,
        water: Int
    ) -> Unit
) {
    var selectedDate by remember {
        mutableStateOf(
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                .format(Date())
        )
    }

    var steps by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var water by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf<String?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = SimpleDateFormat(
                                "MM/dd/yyyy",
                                Locale.getDefault()
                            ).format(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Select", color = FormPrimary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel", color = FormSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = FormHeading,
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp
        ),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 15.dp)
                    .size(width = 40.dp, height = 5.dp)
                    .background(
                        Color(0xFFD5DDE9),
                        CircleShape
                    )
            )
        },
        //windowInsets = WindowInsets.navigationBars
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.91f)
                .imePadding()
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            Color(0xFFF0EEFF),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        tint = FormPrimary,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add Health Record",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FormHeading
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Record your daily activities & vitals",
                        fontSize = 11.sp,
                        color = FormSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            Color(0xFFF1F4F8),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = FormSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFEDF0F5))

            // Scrollable Form Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.height(15.dp))

                // Record Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormLabel(
                        text = "RECORD DATE",
                        required = true
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(Color(0xFFF0F0FF))
                            .padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            )
                    ) {
                        Text(
                            text = "Default: Today",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = FormPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(Color.White)
                        .border(
                            1.dp,
                            FormBorder,
                            RoundedCornerShape(17.dp)
                        )
                        .clickable {
                            showDatePicker = true
                        }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FormIcon(
                        background = Color(0xFFF0EEFF),
                        tint = FormPrimary
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Selected Date",
                            fontSize = 10.sp,
                            color = Color(0xFF91A2BC)
                        )

                        Text(
                            text = selectedDate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FormHeading
                        )
                    }

                    Text(
                        text = "Change",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = FormSecondary
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Choose date",
                        tint = FormSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF91A2BC),
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "Only one record allowed per date. Re-saving updates the entry.",
                        fontSize = 10.sp,
                        color = Color(0xFF91A2BC)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Steps
                RecordInputField(
                    label = "STEPS WALKED",
                    value = steps,
                    onValueChange = {
                        steps = it.filter { char -> char.isDigit() }
                        validationMessage = null
                    },
                    placeholder = "7850",
                    target = "Target: 10,000 steps",
                    unit = "steps",
                    icon = Icons.Default.DirectionsWalk,
                    iconBackground = Color(0xFFF0EEFF),
                    iconTint = FormPrimary,
                    unitBackground = Color(0xFFF0F3F8),
                    unitColor = FormSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Calories
                RecordInputField(
                    label = "CALORIES BURNED",
                    value = calories,
                    onValueChange = {
                        calories = it.filter { char -> char.isDigit() }
                        validationMessage = null
                    },
                    placeholder = "420",
                    target = "Daily Active Energy",
                    unit = "kcal",
                    icon = Icons.Default.LocalFireDepartment,
                    iconBackground = Color(0xFFFFEFEB),
                    iconTint = Color(0xFFFF7043),
                    unitBackground = Color(0xFFFFEFEB),
                    unitColor = Color(0xFFFF7043),
                    targetColor = Color(0xFFFF7043)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Water
                RecordInputField(
                    label = "WATER INTAKE",
                    value = water,
                    onValueChange = {
                        water = it.filter { char -> char.isDigit() }
                        validationMessage = null
                    },
                    placeholder = "2000",
                    target = "Hydration Goal: 2,500 ml",
                    unit = "ml",
                    icon = Icons.Default.WaterDrop,
                    iconBackground = Color(0xFFE5F6FD),
                    iconTint = Color(0xFF16A8E0),
                    unitBackground = Color(0xFFE5F6FD),
                    unitColor = Color(0xFF16A8E0),
                    targetColor = Color(0xFF16A8E0)
                )

                validationMessage?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Fixed Bottom Actions
            HorizontalDivider(color = Color(0xFFEDF0F5))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFC9D5E7)
                    )
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FormHeading
                    )
                }

                Button(
                    onClick = {
                        when {
                            steps.isBlank() ||
                                    calories.isBlank() ||
                                    water.isBlank() -> {
                                validationMessage =
                                    "Please complete all required fields."
                            }

                            steps.toLongOrNull() == null ||
                                    calories.toLongOrNull() == null ||
                                    water.toLongOrNull() == null -> {
                                validationMessage =
                                    "Please enter valid numeric values."
                            }

                            steps.toLong() > Int.MAX_VALUE ||
                                    calories.toLong() > Int.MAX_VALUE ||
                                    water.toLong() > Int.MAX_VALUE -> {
                                validationMessage =
                                    "One or more values are too large."
                            }

                            else -> {
                                onSaveRecord(
                                    selectedDate,
                                    steps.toInt(),
                                    calories.toInt(),
                                    water.toInt()
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FormPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "Save Record",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FormLabel(
    text: String,
    required: Boolean
) {
    Row {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF34445D),
            letterSpacing = 0.3.sp
        )

        if (required) {
            Spacer(modifier = Modifier.width(3.dp))

            Text(
                text = "*",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }
    }
}

@Composable
private fun FormIcon(
    background: Color,
    tint: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                background,
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides tint
        ) {
            content()
        }
    }
}

@Composable
private fun RecordInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    target: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBackground: Color,
    iconTint: Color,
    unitBackground: Color,
    unitColor: Color,
    targetColor: Color = Color(0xFF91A2BC)
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormLabel(
                text = label,
                required = true
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = target,
                fontSize = 10.sp,
                color = targetColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color.White)
                .border(
                    1.dp,
                    FormBorder,
                    RoundedCornerShape(17.dp)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            FormIcon(
                background = iconBackground,
                tint = iconTint
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FormHeading
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 14.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .background(unitBackground)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    )
            ) {
                Text(
                    text = unit,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = unitColor
                )
            }
        }
    }
}
