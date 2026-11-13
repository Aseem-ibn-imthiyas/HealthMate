
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
import androidx.compose.ui.res.stringResource
import com.healthmate.R


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
    sessionCreatedAt: String? = null,
    isPasswordLoading: Boolean = false,
    passwordErrorMessage: String? = null,
    passwordSuccessMessage: String? = null,
    onClearPasswordMessages: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    var validationMessageResId by remember {
        mutableIntStateOf(0)
    }

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
                    deviceName = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}",
                    deviceDetails = "",
                    lastActive = "",
                    location = "",
                    isCurrentDevice = true
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    contentDescription = stringResource(R.string.security_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = stringResource(R.string.settings),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
                    .background(MaterialTheme.colorScheme.surface)
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
                            text = stringResource(R.string.security_change_password),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(R.string.security_password_description),
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PasswordField(
                    label = stringResource(R.string.security_current_password),
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        validationMessageResId = 0
                    },
                    visible = showCurrentPassword,
                    onVisibilityChange = {
                        showCurrentPassword = !showCurrentPassword
                    },
                    leadingIcon = Icons.Default.Key
                )

                Spacer(modifier = Modifier.height(17.dp))

                PasswordField(
                    label = stringResource(R.string.security_new_password),
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        validationMessageResId = 0
                    },
                    visible = showNewPassword,
                    onVisibilityChange = {
                        showNewPassword = !showNewPassword
                    },
                    leadingIcon = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.height(17.dp))

                PasswordField(
                    label = stringResource(R.string.security_confirm_new_password),
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        validationMessageResId = 0
                    },
                    visible = showConfirmPassword,
                    onVisibilityChange = {
                        showConfirmPassword = !showConfirmPassword
                    },
                    leadingIcon = Icons.Default.Lock
                )

                if (validationMessageResId != 0 || passwordErrorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (validationMessageResId != 0) {
                            stringResource(validationMessageResId)
                        } else {
                            passwordErrorMessage.orEmpty()
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(21.dp))

                Button(
                    onClick = {
                        when {
                            currentPassword.isBlank() -> {
                                validationMessageResId =
                                    R.string.security_current_password_required
                            }

                            newPassword.length < 8 -> {
                                validationMessageResId =
                                    R.string.security_password_minimum_length
                            }

                            newPassword == currentPassword -> {
                                validationMessageResId =
                                    R.string.security_password_must_be_different
                            }

                            confirmPassword != newPassword -> {
                                validationMessageResId =
                                    R.string.security_passwords_do_not_match
                            }

                            else -> {
                                onUpdatePassword(
                                    currentPassword,
                                    newPassword
                                )
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
                        text = stringResource(R.string.security_update_password),
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
                    .background(MaterialTheme.colorScheme.surface)
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
                            text = stringResource(R.string.security_login_devices),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(R.string.security_devices_description),
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
    if (passwordSuccessMessage != null) {
        AlertDialog(
            onDismissRequest = {
                onClearPasswordMessages()
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF16A34A)
                )
            },
            title = {
                Text(stringResource(R.string.security_password_updated))
            },
            text = {
                Text(passwordSuccessMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentPassword = ""
                        newPassword = ""
                        confirmPassword = ""
                        onClearPasswordMessages()
                    }
                ) {
                    Text(stringResource(R.string.security_ok))
                    MaterialTheme.colorScheme.primary
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
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = {
                Text(stringResource(R.string.security_sign_out_device_title))
            },
            text = {
                Text(
                    stringResource(
                        R.string.security_sign_out_device_message,
                        device.deviceName
                    )
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
                        stringResource(R.string.security_sign_out),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedDevice = null
                    }
                ) {
                    Text(stringResource(R.string.security_cancel))
                    MaterialTheme.colorScheme.primary
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(
                                    R.string.security_enter_password,
                                    label
                                ),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                contentDescription = stringResource(
                    R.string.security_toggle_password_visibility
                ),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = deviceIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(
                    R.string.security_android_device_details,
                    android.os.Build.VERSION.RELEASE,
                    device.deviceDetails.ifBlank {
                        stringResource(R.string.security_unavailable)
                    }
                ) + if (device.location.isNotBlank()) {
                    " • ${device.location}"
                } else {
                    ""
                },
                fontSize = 10.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(5.dp))

        if (device.isCurrentDevice) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 9.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.security_active_now),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = onSignOutClick)
                    .padding(
                        horizontal = 11.dp,
                        vertical = 9.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.security_sign_out),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
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
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(21.dp)
        )
    }
}
