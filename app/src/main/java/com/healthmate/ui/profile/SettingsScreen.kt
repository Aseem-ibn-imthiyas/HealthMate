
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

private val SettingsBackground = Color(0xFFF7F8FF)
private val SettingsBlue = Color(0xFF0755D9)
private val SettingsHeading = Color(0xFF102641)
private val SettingsSecondary = Color(0xFF59647A)
private val SettingsDivider = Color(0xFFE8EEFA)

@Composable
fun SettingsScreen(
    onManageProfileClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onLanguageChanged: () -> Unit = {},
    selectedLanguage: String = "English",
    onDarkModeChanged: (Boolean) -> Unit = {},
    onNotificationsChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var notificationsEnabled by remember {
        mutableStateOf(false)
    }

    var darkModeEnabled by remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SettingsBackground)
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
                text = "Settings",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = SettingsHeading
            )

            Text(
                text = "Manage & Modify Account",
                fontSize = 12.sp,
                color = SettingsSecondary
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
            ProfileSummaryCard()

            Spacer(modifier = Modifier.height(22.dp))

            // Account
            SettingsSection(title = "ACCOUNT") {

                SettingsGroup {
                    SettingsItem(
                        icon = Icons.Default.PersonOutline,
                        title = "Manage Profile",
                        subtitle = "Personal information",
                        onClick = onManageProfileClick
                    )

                    SettingsDivider()

                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Security & Privacy",
                        subtitle = "Account security",
                        onClick = onSecurityClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Preferences
            SettingsSection(title = "PREFERENCES") {

                SettingsGroup {

                    SettingsToggleItem(
                        icon = Icons.Default.NotificationsNone,
                        title = "Notifications",
                        subtitle = "Push & alerts",
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                            onNotificationsChanged(it)
                        }
                    )

                    SettingsDivider()

                    SettingsToggleItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Match system theme",
                        checked = darkModeEnabled,
                        onCheckedChange = {
                            darkModeEnabled = it
                            onDarkModeChanged(it)
                        }
                    )

                    SettingsDivider()

                    Box {

                        SettingsItem(
                            icon = Icons.Default.Language,
                            title = "Language",
                            subtitle = "App language",
                            trailingContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = selectedLanguage,
                                        fontSize = 11.sp,
                                        color = SettingsBlue,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFFE7F0FF))
                                            .padding(
                                                horizontal = 12.dp,
                                                vertical = 7.dp
                                            )
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = SettingsSecondary
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
            SettingsSection(title = "SUPPORT") {

                SettingsGroup {

                    SettingsItem(
                        icon = Icons.Default.Policy,
                        title = "Terms & Policies",
                        subtitle = "Privacy & terms of service",
                        onClick = onTermsClick
                    )

                    SettingsDivider()

                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About HealthMate",
                        subtitle = "Mission & release notes",
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
                    .background(Color.White)
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
                        text = "Log Out",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )

                    Text(
                        text = "Sign out of your HealthMate account",
                        fontSize = 11.sp,
                        color = SettingsSecondary
                    )
                }

                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Log out",
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
                    tint = SettingsBlue,
                    modifier = Modifier.size(15.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "HealthMate • Version 1.0.0",
                    fontSize = 12.sp,
                    color = SettingsSecondary
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
                    text = "Log Out?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out of HealthMate?"
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
                        "Log Out",
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
                    Text("Cancel")
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun ProfileSummaryCard() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.profile_avatar),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = "Mohamed Aseem",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF20447F)
        )

        Text(
            text = "Aseem@mail.com",
            fontSize = 12.sp,
            color = SettingsSecondary
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
            color = SettingsSecondary,
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
            .background(Color.White)
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
            background = Color(0xFFE6EEFF),
            tint = SettingsBlue
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SettingsHeading
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = SettingsSecondary
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SettingsSecondary,
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
            background = Color(0xFFE6EEFF),
            tint = SettingsBlue
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SettingsHeading
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = SettingsSecondary
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.height(34.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SettingsBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFDCE7FF),
                uncheckedBorderColor = Color(0xFFDCE7FF)
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
