
package com.healthmate.ui.healthrecords

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

private val EditPrimary = Color(0xFF4035AC)
private val EditHeading = Color(0xFF17243B)
private val EditSecondary = Color(0xFF64748B)
private val EditBorder = Color(0xFFDCE4F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHealthRecordScreen(
    recordDate: String = "10/24/2024",
    initialSteps: Int = 7850,
    initialCalories: Int = 420,
    initialWater: Int = 2000,
    onDismiss: () -> Unit,
    onUpdateRecord: (
        date: String,
        steps: Int,
        calories: Int,
        water: Int
    ) -> Unit
) {
    var steps by remember(initialSteps) {
        mutableStateOf(initialSteps.toString())
    }

    var calories by remember(initialCalories) {
        mutableStateOf(initialCalories.toString())
    }

    var water by remember(initialWater) {
        mutableStateOf(initialWater.toString())
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
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
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = EditPrimary,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Edit Health Record",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditHeading
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Edit your daily activities & vitals",
                        fontSize = 11.sp,
                        color = EditSecondary
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
                        tint = EditSecondary
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFEDF0F5))

            // Form Content
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditFieldLabel(
                        text = "RECORD DATE"
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = "*",
                        color = Color.Red,
                        fontSize = 12.sp
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
                            EditBorder,
                            RoundedCornerShape(17.dp)
                        )
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    EditIconBox(
                        background = Color(0xFFF0EEFF),
                        tint = EditPrimary
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Selected Date",
                            fontSize = 10.sp,
                            color = Color(0xFF91A2BC)
                        )

                        Text(
                            text = recordDate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EditHeading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF91A2BC),
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "Date Can't Edit",
                        fontSize = 10.sp,
                        color = Color(0xFF91A2BC)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Steps
                EditRecordInput(
                    label = "STEPS WALKED",
                    value = steps,
                    onValueChange = {
                        steps = it.filter(Char::isDigit)
                        validationMessage = null
                    },
                    target = "Target: 10,000 steps",
                    unit = "steps",
                    icon = Icons.Default.DirectionsWalk,
                    iconBackground = Color(0xFFF0EEFF),
                    iconTint = EditPrimary,
                    unitBackground = Color(0xFFF0F3F8),
                    unitColor = EditSecondary
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Calories
                EditRecordInput(
                    label = "CALORIES BURNED",
                    value = calories,
                    onValueChange = {
                        calories = it.filter(Char::isDigit)
                        validationMessage = null
                    },
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
                EditRecordInput(
                    label = "WATER INTAKE",
                    value = water,
                    onValueChange = {
                        water = it.filter(Char::isDigit)
                        validationMessage = null
                    },
                    target = "Hydration Goal: 2,500 ml",
                    unit = "ml",
                    icon = Icons.Default.WaterDrop,
                    iconBackground = Color(0xFFE5F6FD),
                    iconTint = Color(0xFF16A8E0),
                    unitBackground = Color(0xFFE5F6FD),
                    unitColor = Color(0xFF16A8E0),
                    targetColor = Color(0xFF16A8E0)
                )

                validationMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = it,
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
                        color = EditHeading
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
                                onUpdateRecord(
                                    recordDate,
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
                        containerColor = EditPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "Edit Record",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EditFieldLabel(
    text: String
) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF34445D),
        letterSpacing = 0.3.sp
    )
}

@Composable
private fun EditIconBox(
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
private fun EditRecordInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
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
            EditFieldLabel(label)

            Text(
                text = " *",
                color = Color.Red,
                fontSize = 12.sp
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
                    EditBorder,
                    RoundedCornerShape(17.dp)
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            EditIconBox(
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
                    color = EditHeading
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
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
