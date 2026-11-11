
package com.healthmate.ui.healthrecords.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.healthmate.ui.healthrecords.model.HealthRecordUiModel

private val DeleteRed = Color(0xFFF44343)
private val DeleteHeading = Color(0xFF183C75)
private val DeleteSecondary = Color(0xFF526A8A)
private val DeleteBorder = Color(0xFFDCE7F7)

@Composable
fun DeleteHealthRecordDialog(
    record: HealthRecordUiModel,
    displayDate: String = record.date,
    onDismiss: () -> Unit,
    onConfirmDelete: (HealthRecordUiModel) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Warning Icon
            Box(
                modifier = Modifier
                    .size(53.dp)
                    .background(
                        Color(0xFFFFE2E2),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = "Warning",
                    tint = DeleteRed,
                    modifier = Modifier.size(29.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Dialog Title
            Text(
                text = "Delete Health Record?",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = DeleteHeading,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = "Are you sure you want to delete this health record? This action cannot be undone.",
                fontSize = 13.sp,
                lineHeight = 21.sp,
                color = DeleteSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Selected Record Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFFF6F8FD))
                    .border(
                        1.dp,
                        DeleteBorder,
                        RoundedCornerShape(40.dp)
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 13.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFE4ECFF),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(17.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = displayDate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeleteHeading
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "%,d steps".format(record.steps),
                            fontSize = 10.sp,
                            color = DeleteSecondary
                        )

                        Text(
                            text = " • ",
                            fontSize = 10.sp,
                            color = DeleteSecondary
                        )

                        Text(
                            text = "${record.calories} kcal",
                            fontSize = 10.sp,
                            color = DeleteSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Cancel
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(30.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        DeleteBorder
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = "No, Keep It",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeleteHeading,
                        maxLines = 1
                    )
                }

                // Confirm Delete
                Button(
                    onClick = {
                        onConfirmDelete(record)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeleteRed
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "Yes, Delete",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
