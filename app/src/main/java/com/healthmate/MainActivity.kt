
package com.healthmate

import com.healthmate.ui.communities.CommunitiesScreen
import com.healthmate.ui.healthrecords.ViewHealthRecordDetailsScreen
import androidx.compose.ui.draw.blur
import com.healthmate.ui.healthrecords.components.DeleteHealthRecordDialog
import androidx.compose.runtime.*
import com.healthmate.ui.healthrecords.EditHealthRecordScreen
import androidx.compose.runtime.*
import com.healthmate.ui.healthrecords.model.HealthRecordUiModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.healthmate.ui.components.AnimatedBottomNavigationBar
import com.healthmate.ui.components.HealthMateDestination
import com.healthmate.ui.healthrecords.AddHealthRecordScreen
import com.healthmate.ui.healthrecords.HealthRecordsScreen
import com.healthmate.ui.communities.CreateCommunityScreen
import com.healthmate.ui.communities.model.CreateCommunityUiModel
import com.healthmate.ui.communities.ViewCommunityScreen
import com.healthmate.ui.communities.model.CommunityUiModel
import com.healthmate.ui.dashboard.DashboardScreen
import com.healthmate.ui.profile.AboutHealthMateScreen
import com.healthmate.ui.profile.SettingsScreen
import com.healthmate.ui.profile.EditProfileScreen
import com.healthmate.ui.profile.model.EditProfileUiModel
import com.healthmate.ui.profile.SecurityPrivacyScreen
import com.healthmate.ui.profile.LanguageScreen
import com.healthmate.ui.profile.TermsPoliciesScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                var selectedDestination by remember {
                    mutableStateOf(HealthMateDestination.DASHBOARD)
                }

                var selectedDetailsRecord by remember {
                    mutableStateOf<HealthRecordUiModel?>(null)
                }

                var showDeleteDialog by remember {
                    mutableStateOf(false)
                }

                var selectedDeleteRecord by remember {
                    mutableStateOf<HealthRecordUiModel?>(null)
                }

                var showEditRecord by remember {
                    mutableStateOf(false)
                }

                var selectedRecord by remember {
                    mutableStateOf<HealthRecordUiModel?>(null)
                }

                var showAddRecord by remember {
                    mutableStateOf(false)
                }

                var showCreateCommunity by remember {
                    mutableStateOf(false)
                }

                var selectedCommunity by remember {
                    mutableStateOf<CommunityUiModel?>(null)
                }

                var showEditProfile by remember {
                    mutableStateOf(false)
                }

                var showSecurityPrivacy by remember {
                    mutableStateOf(false)
                }

                var showLanguageScreen by remember {
                    mutableStateOf(false)
                }

                var selectedAppLanguage by remember {
                    mutableStateOf("en")
                }

                var showTermsPolicies by remember {
                    mutableStateOf(false)
                }

                var showAboutScreen by remember {
                    mutableStateOf(false)
                }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .blur(if (showDeleteDialog) 7.dp else 0.dp)
                    ) {
                        when (selectedDestination) {

                            HealthMateDestination.DASHBOARD -> {
                                DashboardScreen()
                            }

                            HealthMateDestination.COMMUNITIES -> {

                                val community = selectedCommunity

                                if (community != null) {

                                    // View Selected Community
                                    ViewCommunityScreen(
                                        community = community,
                                        onBackClick = {
                                            selectedCommunity = null
                                        },
                                        onSharePost = { content ->

                                            // Post INSERT operation later

                                        },
                                        onCommentClick = { post ->

                                            // Comments screen later

                                        },
                                        onSharePostClick = { post ->

                                            // Share functionality later

                                        }
                                    )

                                } else if (showCreateCommunity) {

                                    // Create Community
                                    CreateCommunityScreen(
                                        onBackClick = {
                                            showCreateCommunity = false
                                        },
                                        onCancelClick = {
                                            showCreateCommunity = false
                                        },
                                        onCreateCommunity = { community ->

                                            // Community database INSERT later

                                            showCreateCommunity = false
                                        }
                                    )

                                } else {

                                    // Communities Main Screen
                                    CommunitiesScreen(
                                        onCreateCommunityClick = {
                                            showCreateCommunity = true
                                        },
                                        onViewAllCategoriesClick = {

                                            // View all categories later

                                        },
                                        onCommunityClick = { community ->
                                            selectedCommunity = community
                                        }
                                    )
                                }
                            }

                            HealthMateDestination.HEALTH_RECORDS -> {
                                val detailsRecord = selectedDetailsRecord

                                if (detailsRecord != null) {
                                    ViewHealthRecordDetailsScreen(
                                        record = detailsRecord,
                                        onBackClick = {
                                            selectedDetailsRecord = null
                                        },
                                        displayDate = "Saturday, October 24",
                                        dateBadge = "Oct 24 • Today",
                                        isSynced = true,
                                        stepsTarget = 10000,
                                        waterTarget = 2500,
                                        deviceSource = "Pixel 7 Sensors"
                                    )
                                } else {
                                    HealthRecordsScreen(
                                        onAddRecordClick = {
                                            showAddRecord = true
                                        },
                                        onEditRecordClick = { record ->
                                            selectedRecord = record
                                            showEditRecord = true
                                        },
                                        onDeleteRecordClick = { record ->
                                            selectedDeleteRecord = record
                                            showDeleteDialog = true
                                        },
                                        onRecordClick = { record ->
                                            selectedDetailsRecord = record
                                        }
                                    )
                                }
                            }


                            HealthMateDestination.PROFILE -> {

                                when {

                                    showAboutScreen -> {
                                        AboutHealthMateScreen(
                                            onBackClick = {
                                                showAboutScreen = false
                                            }
                                        )
                                    }

                                    showTermsPolicies -> {
                                        TermsPoliciesScreen(
                                            onBackClick = {
                                                showTermsPolicies = false
                                            }
                                        )
                                    }

                                    // Language Screen
                                    showLanguageScreen -> {
                                        LanguageScreen(
                                            currentLanguage = selectedAppLanguage,
                                            onBackClick = {
                                                showLanguageScreen = false
                                            },
                                            onSaveLanguage = { languageCode ->
                                                selectedAppLanguage = languageCode
                                                showLanguageScreen = false
                                            }
                                        )
                                    }

                                    // Security & Privacy Screen
                                    showSecurityPrivacy -> {
                                        SecurityPrivacyScreen(
                                            onBackClick = {
                                                showSecurityPrivacy = false
                                            },
                                            onUpdatePassword = { currentPassword, newPassword ->

                                                // Password update logic later.

                                            },
                                            onSignOutDevice = { device ->

                                                // Device session management later.

                                            }
                                        )
                                    }

                                    // Edit Profile Screen
                                    showEditProfile -> {
                                        EditProfileScreen(
                                            onBackClick = {
                                                showEditProfile = false
                                            },
                                            onCancelClick = {
                                                showEditProfile = false
                                            },
                                            onSaveChanges = { updatedProfile ->

                                                // Room database UPDATE later.

                                                showEditProfile = false
                                            }
                                        )
                                    }

                                    // Settings Main Screen
                                    else -> {
                                        SettingsScreen(
                                            onManageProfileClick = {
                                                showEditProfile = true
                                            },
                                            onSecurityClick = {
                                                showSecurityPrivacy = true
                                            },
                                            onLanguageChanged = {
                                                showLanguageScreen = true
                                            },
                                            selectedLanguage = when (selectedAppLanguage) {
                                                "en" -> "English"
                                                "si" -> "Sinhala"
                                                "ta" -> "Tamil"
                                                else -> "English"
                                            },
                                            onTermsClick = {
                                                showTermsPolicies = true
                                            },
                                            onAboutClick = {
                                                showAboutScreen = true
                                            },
                                            onLogoutClick = {
                                                // Logout later
                                            }
                                        )
                                    }
                                }
                            }


                            else -> {
                                // Other screens will be connected later.
                            }
                        }
                    }

                    selectedRecord?.let { record ->
                        if (showEditRecord) {
                            EditHealthRecordScreen(
                                recordDate = record.date,
                                initialSteps = record.steps,
                                initialCalories = record.calories,
                                initialWater = record.water,
                                onDismiss = {
                                    showEditRecord = false
                                },
                                onUpdateRecord = { date, steps, calories, water ->

                                    // Room database UPDATE operation later.

                                    showEditRecord = false
                                }
                            )
                        }
                    }

                    selectedDeleteRecord?.let { record ->

                        if (showDeleteDialog) {
                            DeleteHealthRecordDialog(
                                record = record,
                                onDismiss = {
                                    showDeleteDialog = false
                                    selectedDeleteRecord = null
                                },
                                onConfirmDelete = { selectedRecord ->

                                    // Room database DELETE operation later.

                                    showDeleteDialog = false
                                    selectedDeleteRecord = null
                                }
                            )
                        }
                    }

                    AnimatedBottomNavigationBar(
                        selectedDestination = selectedDestination,
                        onDestinationSelected = {
                            selectedDestination = it
                        },
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .navigationBarsPadding()
                            .padding(bottom = 10.dp)
                    )
                }

                // Add Health Record Modal
                if (showAddRecord) {
                    AddHealthRecordScreen(
                        onDismiss = {
                            showAddRecord = false
                        },
                        onSaveRecord = { date, steps, calories, water ->

                            // Room database integration later.
                            // Save the record through the ViewModel.

                            showAddRecord = false
                        }
                    )
                }
            }
        }
    }
}
