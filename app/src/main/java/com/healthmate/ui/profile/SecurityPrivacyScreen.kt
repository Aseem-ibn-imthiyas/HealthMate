
package com.healthmate.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.profile.model.LoginDeviceUiModel

private val SecurityBlue = Color(0xFF0755D9)
private val SecurityHeading = Color(0xFF102641)
private val SecuritySecondary = Color(0xFF59647A)
private val SecurityBackground = Color(0xFFF7F8FF)
private val SecurityField = Color(0xFFEEF3FF)

@Composable
fun SecurityPrivacyScreen(
    onBackClick: () -> Unit,
    onUpdatePassword: (
        currentPassword: String,
        newPassword: String
    ) -> Unit,
    onSignOutDevice: (LoginDeviceUiModel) -> Unit,
    modifier: Modifier = Modifier
) {

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var validationMessage by remember { mutableStateOf<String?>(null) }

    var selectedDevice by remember {
        mutableStateOf<LoginDeviceUiModel?>(null)
    }

    var showPasswordSuccess by remember {
        mutableStateOf(false)
    }

    var devices by remember {
        mutableStateOf(
            listOf(
                LoginDeviceUiModel(
                    id = 1,
                    deviceName = "Google Pixel 7",
                    deviceDetails = "Android 14 • HealthMate v2.4",
                    lastActive = "Active now",
                    location = "",
                    isCurrentDevice = true
                ),
                LoginDeviceUiModel(
                    id = 2,
                    deviceName = "Chrome on macOS",
                    deviceDetails = "Last active 2 hours ago",
                    lastActive = "Sign Out",
                    location = "Berkeley, CA",
                    isCurrentDevice = false
                ),
                LoginDeviceUiModel(
                    id = 3,
                    deviceName = "HealthMate for iPad",
                    deviceDetails = "Last active Oct 22, 2024",
                    lastActive = "Sign Out",
                    location = "San Jose, CA",
                    isCurrentDevice = false
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SecurityBackground)
            .statusBarsPadding()
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = SecurityHeading,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Settings",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = SecurityHeading
            )
        }

        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(
                    androidx.compose.foundation.rememberScrollState()
                )
                .padding(
                    horizontal = 32.dp,
                    vertical = 15.dp
                ),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            // Change Password Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(29.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecurityIcon(
                        icon = Icons.Default.LockReset
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Change Password",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SecurityHeading
                        )

                        Text(
                            text = "Update your account credentials to keep health records safe.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = SecuritySecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PasswordField(
                    label = "Current Password",
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        validationMessage = null
                    },
                    visible = showCurrentPassword,
                    onVisibilityChange = {
                        showCurrentPassword = !showCurrentPassword
                    },
                    leadingIcon = Icons.Default.Key
                )

                Spacer(modifier = Modifier.height(17.dp))

                PasswordField(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        validationMessage = null
                    },
                    visible = showNewPassword,
                    onVisibilityChange = {
                        showNewPassword = !showNewPassword
                    },
                    leadingIcon = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(17.dp))

                PasswordField(
                    label = "Confirm New Password",
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        validationMessage = null
                    },
                    visible = showConfirmPassword,
                    onVisibilityChange = {
                        showConfirmPassword = !showConfirmPassword
                    },
                    leadingIcon = Icons.Default.Lock
                )

                validationMessage?.let {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(21.dp))

                Button(
                    onClick = {
                        when {
                            currentPassword.isBlank() -> {
                                validationMessage =
                                    "Please enter your current password."
                            }

                            newPassword.length < 8 -> {
                                validationMessage =
                                    "New password must contain at least 8 characters."
                            }

                            newPassword == currentPassword -> {
                                validationMessage =
                                    "New password must be different from the current password."
                            }

                            confirmPassword != newPassword -> {
                                validationMessage =
                                    "New passwords do not match."
                            }

                            else -> {
                                onUpdatePassword(
                                    currentPassword,
                                    newPassword
                                )

                                currentPassword = ""
                                newPassword = ""
                                confirmPassword = ""

                                showPasswordSuccess = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0752CC)
                    )
                ) {
                    Text(
                        text = "Update Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Login Devices Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(29.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecurityIcon(
                        icon = Icons.Default.Devices
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Login Devices",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SecurityHeading
                        )

                        Text(
                            text = "Review all authenticated sessions for this account.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = SecuritySecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(19.dp))

                devices.forEach { device ->

                    LoginDeviceItem(
                        device = device,
                        onSignOutClick = {
                            selectedDevice = device
                        }
                    )

                    if (device.id != devices.last().id) {
                        Spacer(modifier = Modifier.height(9.dp))
                    }
                }
            }
        }
    }

    // Password Update Confirmation
    if (showPasswordSuccess) {
        AlertDialog(
            onDismissRequest = {
                showPasswordSuccess = false
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF16A34A)
                )
            },
            title = {
                Text("Password Updated")
            },
            text = {
                Text("Your password update request was submitted.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPasswordSuccess = false
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Device Sign Out Confirmation
    selectedDevice?.let { device ->
        AlertDialog(
            onDismissRequest = {
                selectedDevice = null
            },
            title = {
                Text("Sign Out Device?")
            },
            text = {
                Text(
                    "Are you sure you want to sign out ${device.deviceName}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        devices = devices.filter {
                            it.id != device.id
                        }

                        onSignOutDevice(device)
                        selectedDevice = null
                    }
                ) {
                    Text(
                        "Sign Out",
                        color = Color(0xFFDC2626)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedDevice = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onVisibilityChange: () -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column {

        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = SecuritySecondary
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
                .clip(CircleShape)
                .background(SecurityField)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = SecuritySecondary,
                modifier = Modifier.size(19.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (visible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = SecurityHeading
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = "Enter $label",
                                fontSize = 13.sp,
                                color = Color(0xFF91A0B7)
                            )
                        }

                        innerTextField()
                    }
                }
            )

            Icon(
                imageVector = if (visible) {
                    Icons.Default.VisibilityOff
                } else {
                    Icons.Default.Visibility
                },
                contentDescription = "Toggle password visibility",
                tint = SecuritySecondary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(onClick = onVisibilityChange)
            )
        }
    }
}

@Composable
private fun LoginDeviceItem(
    device: LoginDeviceUiModel,
    onSignOutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(SecurityField)
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val deviceIcon = when {
            device.deviceName.contains("Pixel", ignoreCase = true) ->
                Icons.Default.Smartphone

            device.deviceName.contains("Chrome", ignoreCase = true) ->
                Icons.Default.LaptopMac

            else -> Icons.Default.Tablet
        }

        Box(
            modifier = Modifier
                .size(39.dp)
                .background(Color(0xFFDCE9FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = deviceIcon,
                contentDescription = null,
                tint = SecuritySecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = device.deviceName,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SecurityHeading
            )

            Text(
                text = listOf(
                    device.deviceDetails,
                    device.location
                ).filter { it.isNotBlank() }.joinToString(" • "),
                fontSize = 10.sp,
                lineHeight = 15.sp,
                color = SecuritySecondary
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        if (device.isCurrentDevice) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFDCE8FF))
                    .padding(
                        horizontal = 10.dp,
                        vertical = 9.dp
                    )
            ) {
                Text(
                    text = "Active now",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecurityHeading
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFD9E6F8))
                    .clickable(onClick = onSignOutClick)
                    .padding(
                        horizontal = 11.dp,
                        vertical = 9.dp
                    )
            ) {
                Text(
                    text = "Sign Out",
                    fontSize = 10.sp,
                    color = SecurityHeading
                )
            }
        }
    }
}

@Composable
private fun SecurityIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color(0xFFD9E7FF), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SecurityBlue,
            modifier = Modifier.size(21.dp)
        )
    }
}
