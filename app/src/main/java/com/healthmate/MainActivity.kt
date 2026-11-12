
package com.healthmate

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import com.healthmate.ui.onboarding.GetStartedScreen
import com.healthmate.ui.signin.SignInScreen
import com.healthmate.ui.signup.SignUpScreen
import androidx.compose.runtime.LaunchedEffect
import com.healthmate.data.repository.UserRepository
import com.healthmate.viewmodel.AuthViewModel
import com.healthmate.viewmodel.AuthViewModelFactory
import com.healthmate.ui.communities.CommunitiesScreen
import com.healthmate.ui.healthrecords.ViewHealthRecordDetailsScreen
import androidx.compose.ui.draw.blur
import com.healthmate.ui.healthrecords.components.DeleteHealthRecordDialog
import androidx.compose.runtime.*
import com.healthmate.ui.healthrecords.EditHealthRecordScreen
import com.healthmate.ui.healthrecords.model.HealthRecordUiModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.healthmate.data.local.database.HealthMateDatabase
import com.healthmate.data.repository.HealthRecordRepository
import com.healthmate.viewmodel.HealthRecordViewModel
import com.healthmate.viewmodel.HealthRecordViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.healthmate.data.local.entity.HealthRecord
import com.healthmate.ui.forgotpassword.ForgotPasswordScreen
import com.healthmate.ui.forgotpassword.ResetPasswordScreen
import java.util.Calendar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                // Initialize Room Database
                val database = remember {
                    HealthMateDatabase.getDatabase(applicationContext)
                }

                // Initialize Repository
                val repository = remember {
                    HealthRecordRepository(database.healthRecordDao())
                }

                // Initialize ViewModel
                val healthRecordViewModel: HealthRecordViewModel = viewModel(
                    factory = HealthRecordViewModelFactory(repository)
                )

                // Initialize User Repository
                val userRepository = remember {
                    UserRepository(database.userDao())
                }

                // Initialize Auth ViewModel
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(
                        userRepository = userRepository,
                        userSessionDao = database.userSessionDao()
                    )
                )

                // Observe Authentication State
                val authUiState by authViewModel.uiState
                    .collectAsStateWithLifecycle()

                // Authentication navigation state
                var authScreen by remember {
                    mutableStateOf("loading")
                }

                var resetEmail by remember { mutableStateOf("") }

                // Handle session restoration and authentication navigation
                LaunchedEffect(
                    authUiState.isCheckingSession,
                    authUiState.isLoggedIn,
                    authUiState.successMessage
                ) {
                    when {
                        authUiState.isCheckingSession -> {
                            authScreen = "loading"
                        }

                        authUiState.isLoggedIn -> {
                            authScreen = "dashboard"
                        }

                        authUiState.successMessage == "Account created successfully." -> {
                            authScreen = "signin"
                        }

                        authScreen == "loading" || authScreen == "dashboard" -> {
                            authScreen = "get_started"
                        }
                    }
                }

                LaunchedEffect(authUiState.isResetEmailRegistered) {
                    if (authUiState.isResetEmailRegistered == true) {
                        authScreen = "reset_password"
                    }
                }

                LaunchedEffect(authUiState.successMessage) {
                    if (authUiState.successMessage == "Password reset successfully.") {
                        authViewModel.clearMessages()
                        authScreen = "signin"
                    }
                }

                // Observe ViewModel State
                val healthRecordUiState by healthRecordViewModel.uiState
                    .collectAsStateWithLifecycle()

                // Reusable Room record to UI model conversion
                val convertToUiModel: (HealthRecord) -> HealthRecordUiModel = { record ->

                    val recordDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.US
                    ).parse(record.date)

                    val displayDate = recordDate?.let {
                        SimpleDateFormat("MMM dd", Locale.US).format(it)
                    } ?: record.date

                    val today = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.US
                    ).format(Date())

                    val yesterday = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.US
                    ).format(
                        Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
                    )

                    val dateLabel = when (record.date) {
                        today -> "Today"
                        yesterday -> "Yesterday"
                        else -> "Logged"
                    }

                    HealthRecordUiModel(
                        id = record.id,
                        date = displayDate,
                        dateLabel = dateLabel,
                        steps = record.steps,
                        calories = record.calories,
                        water = record.water,
                        updatedAt = "Saved locally",
                        dateIso = record.date
                    )
                }

                // Convert all Room records into UI records
                val displayRecords = remember(healthRecordUiState.records) {
                    healthRecordUiState.records.map(convertToUiModel)
                }

                // Convert search results into UI records
                val searchDisplayRecords = remember(healthRecordUiState.searchResults) {
                    healthRecordUiState.searchResults.map(convertToUiModel)
                }

                // Calculate records logged during the last 7 days
                val weekStartCalendar = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -6)
                }

                val weekStartDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                ).format(weekStartCalendar.time)

                val todayDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                ).format(Date())

                val weeklyRecordCount = healthRecordUiState.records.count { record ->
                    record.date >= weekStartDate && record.date <= todayDate
                }

                var isSearchActive by remember {
                    mutableStateOf(false)
                }

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

                    if (authScreen != "dashboard") {

                        when (authScreen) {

                            "loading" -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color(0xFF2563EB)
                                    )
                                }
                            }

                            "get_started" -> {
                                GetStartedScreen(
                                    onGetStartedClick = {
                                        authScreen = "signin"
                                    }
                                )
                            }

                            "signin" -> {
                                SignInScreen(
                                    onSignInClick = { email, password ->
                                        authViewModel.loginUser(email, password)
                                    },
                                    onForgotPasswordClick = {
                                        authScreen = "forgot_password"
                                    },
                                    onSignUpClick = {
                                        authScreen = "signup"
                                    },
                                    errorMessage = authUiState.errorMessage
                                )
                            }

                            "signup" -> {
                                SignUpScreen(
                                    onSignUpClick = { fullName, email, password ->
                                        authViewModel.registerUser(
                                            fullName = fullName,
                                            email = email,
                                            password = password
                                        )
                                    },
                                    onSignInClick = {
                                        authScreen = "signin"
                                    },
                                    onTermsClick = {
                                        // Terms screen navigation will be connected later.
                                    },
                                    onPrivacyClick = {
                                        // Privacy screen navigation will be connected later.
                                    },
                                    errorMessage = authUiState.errorMessage
                                )
                            }

                            "forgot_password" -> {
                                ForgotPasswordScreen(
                                    onContinueClick = { email ->
                                        resetEmail = email
                                        authViewModel.checkResetEmail(email)
                                    },
                                    onSignInClick = {
                                        authViewModel.clearMessages()
                                        authScreen = "signin"
                                    },
                                    isLoading = authUiState.isLoading,
                                    errorMessage = authUiState.errorMessage
                                )
                            }

                            "reset_password" -> {
                                ResetPasswordScreen(
                                    onResetPasswordClick = { password, confirmPassword ->
                                        if (password == confirmPassword) {
                                            authViewModel.resetPassword(
                                                email = resetEmail,
                                                newPassword = password
                                            )
                                        }
                                    },
                                    onSignInClick = {
                                        authViewModel.clearMessages()
                                        authScreen = "signin"
                                    },
                                    isLoading = authUiState.isLoading,
                                    errorMessage = authUiState.errorMessage
                                )
                            }
                        }

                    } else {

                        // Keep your existing main application Column content here.
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .blur(if (showDeleteDialog) 7.dp else 0.dp)
                    ) {
                        when (selectedDestination) {

                            HealthMateDestination.DASHBOARD -> {
                                DashboardScreen(
                                    todaySteps = healthRecordUiState.todayRecord?.steps ?: 0,
                                    todayCalories = healthRecordUiState.todayRecord?.calories ?: 0,
                                    todayWater = healthRecordUiState.todayRecord?.water ?: 0,
                                    weeklyRecordCount = weeklyRecordCount
                                )
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
                                        onBackClick = {selectedDetailsRecord = null},
                                        isSynced = true,
                                        stepsTarget = 10000,
                                        waterTarget = 2500,
                                        deviceSource = "Pixel 7 Sensors"
                                    )
                                } else {
                                    HealthRecordsScreen(
                                        records = displayRecords,
                                        searchResults = searchDisplayRecords,

                                        onClearSearch = {
                                            healthRecordViewModel.clearSearch()
                                            //isSearchActive = false
                                        },

                                        onSearchByDate = { date ->
                                            healthRecordViewModel.searchRecords(date)
                                        },

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
                                                authViewModel.logoutUser()
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
                                    selectedRecord = null
                                },

                                onUpdateRecord = { date, steps, calories, water ->

                                    // Update existing record in Room Database
                                    val updatedRecord = HealthRecord(
                                        id = record.id,
                                        date = record.dateIso,
                                        steps = steps,
                                        calories = calories,
                                        water = water
                                    )

                                    healthRecordViewModel.updateRecord(updatedRecord)

                                    showEditRecord = false
                                    selectedRecord = null
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

                                onConfirmDelete = {

                                    // Delete selected record from Room Database
                                    val recordToDelete = HealthRecord(
                                        id = record.id,
                                        date = record.dateIso,
                                        steps = record.steps,
                                        calories = record.calories,
                                        water = record.water
                                    )

                                    healthRecordViewModel.deleteRecord(recordToDelete)

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

                            // Save health record through ViewModel
                            healthRecordViewModel.saveRecord(
                                date = date,
                                steps = steps,
                                calories = calories,
                                water = water
                            )

                            showAddRecord = false
                        }
                    )
                }
            }
        }
    }
}
