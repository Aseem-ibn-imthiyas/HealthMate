
package com.healthmate.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.R
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource

private val SettingsBackground = Color(0xFFF7F8FF)
private val SettingsBlue = Color(0xFF0755D9)
private val SettingsHeading = Color(0xFF102641)
private val SettingsSecondary = Color(0xFF59647A)
private val SettingsDivider = Color(0xFFE8EEFA)

@Composable
fun SettingsScreen(
    userFullName: String = "",
    userEmail: String = "",
    userProfileImageUri: String? = null,
    darkModeEnabled: Boolean = false,
    onManageProfileClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onLanguageChanged: () -> Unit = {},
    selectedLanguage: String = "English",
    onDarkModeChanged: (Boolean) -> Unit = {},
    initialNotificationsEnabled: Boolean = false,
    onNotificationsChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var notificationsEnabled by remember(initialNotificationsEnabled) {
        mutableStateOf(initialNotificationsEnabled)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            notificationsEnabled = granted

            if (granted) {
                onNotificationsChanged(true)
            } else {
                onNotificationsChanged(false)
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        // Main Header
        Column(
            modifier = Modifier.padding(
                start = 32.dp,
                end = 32.dp,
                top = 24.dp,
                bottom = 24.dp
            )
        ) {
            Text(
                text = stringResource(R.string.settings),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = stringResource(R.string.settings_manage_modify_account),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Scrollable Settings Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 32.dp)
                .verticalScroll(
                    androidx.compose.foundation.rememberScrollState()
                )
        ) {

            // Profile Summary Card
            ProfileSummaryCard(
                fullName = userFullName,
                email = userEmail,
                profileImageUri = userProfileImageUri
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Account
            SettingsSection(
                title = stringResource(R.string.settings_account)
            ) {

                SettingsGroup {
                    SettingsItem(
                        icon = Icons.Default.PersonOutline,
                        title = stringResource(R.string.settings_manage_profile),
                        subtitle = stringResource(R.string.settings_personal_information),
                        onClick = onManageProfileClick
                    )

                    SettingsDivider()

                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = stringResource(R.string.settings_security_privacy),
                        subtitle = stringResource(R.string.settings_account_security),
                        onClick = onSecurityClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Preferences
            SettingsSection(
                title = stringResource(R.string.settings_preferences)
            ) {

                SettingsGroup {

                    SettingsToggleItem(
                        icon = Icons.Default.NotificationsNone,
                        title = stringResource(R.string.settings_notifications),
                        subtitle = stringResource(R.string.settings_push_alerts),
                        checked = notificationsEnabled,
                        onCheckedChange = { enabled ->

                            if (enabled && Build.VERSION.SDK_INT >= 33) {

                                if (
                                    context.checkSelfPermission(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    notificationsEnabled = true
                                    onNotificationsChanged(true)

                                } else {
                                    notificationPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }

                            } else {
                                notificationsEnabled = enabled
                                onNotificationsChanged(enabled)
                            }
                        }
                    )

                    SettingsDivider()


                    SettingsToggleItem(
                        icon = Icons.Default.DarkMode,
                        title = stringResource(R.string.settings_dark_mode),
                        subtitle = stringResource(R.string.settings_match_system_theme),
                        checked = darkModeEnabled,
                        onCheckedChange = { enabled ->
                            //darkModeEnabled = enabled
                            onDarkModeChanged(enabled)
                        }
                    )


                    SettingsDivider()

                    Box {

                        SettingsItem(
                            icon = Icons.Default.Language,
                            title = stringResource(R.string.settings_language),
                            subtitle = stringResource(R.string.settings_app_language),
                            trailingContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedLanguage,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(
                                                horizontal = 12.dp,
                                                vertical = 7.dp
                                            )
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = onLanguageChanged
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Support
            SettingsSection(
                title = stringResource(R.string.settings_support)
            ) {

                SettingsGroup {

                    SettingsItem(
                        icon = Icons.Default.Policy,
                        title = stringResource(R.string.settings_terms_policies),
                        subtitle = stringResource(R.string.settings_privacy_terms),
                        onClick = onTermsClick
                    )

                    SettingsDivider()

                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.settings_about_healthmate),
                        subtitle = stringResource(R.string.settings_mission_release_notes),
                        onClick = onAboutClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            // Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        showLogoutDialog = true
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SettingsIcon(
                    icon = Icons.Default.Logout,
                    background = Color(0xFFFFDEDC),
                    tint = Color(0xFFDC2626)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.settings_logout),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )

                    Text(
                        text = stringResource(R.string.settings_logout_description),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = stringResource(R.string.settings_logout_accessibility),
                    tint = Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // App Version
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = stringResource(R.string.settings_app_version),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            icon = {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(30.dp)
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.settings_logout_confirmation_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.settings_logout_confirmation_message)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text(
                        stringResource(R.string.settings_logout),
                        color = Color(0xFFDC2626)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(stringResource(R.string.settings_cancel))
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun ProfileSummaryCard(
    fullName: String,
    email: String,
    profileImageUri: String?
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = if (!profileImageUri.isNullOrBlank()) {
                coil.compose.rememberAsyncImagePainter(
                    model = profileImageUri
                )
            } else {
                painterResource(R.drawable.profile_avatar)
            },
            contentDescription = stringResource(R.string.profile_picture),
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = fullName.ifBlank {
                stringResource(R.string.healthmate_user)
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = email,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                start = 4.dp,
                bottom = 9.dp
            )
        )

        content()
    }
}

@Composable
private fun SettingsGroup(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        content = content
    )
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon,
            background = MaterialTheme.colorScheme.surfaceVariant,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon,
            background = MaterialTheme.colorScheme.surfaceVariant,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.height(34.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
private fun SettingsIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    background: Color,
    tint: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(19.dp)
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 3.dp),
        thickness = 1.dp,
        color = SettingsDivider
    )
}
