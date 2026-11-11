
package com.healthmate.ui.profile

import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.healthmate.R
import com.healthmate.ui.profile.model.EditProfileUiModel

private val ProfileBlue = Color(0xFF0755D9)
private val ProfileDarkBlue = Color(0xFF20447F)
private val ProfileHeading = Color(0xFF102641)
private val ProfileSecondary = Color(0xFF59647A)
private val ProfileBackground = Color(0xFFF7F8FF)
private val ProfileFieldBackground = Color(0xFFEEF3FF)
private val ProfileBorder = Color(0xFFE4EAF5)

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveChanges: (EditProfileUiModel) -> Unit,
    onCancelClick: () -> Unit = onBackClick,
    modifier: Modifier = Modifier
) {

    var fullName by remember {
        mutableStateOf("Mohamed Aseem")
    }

    var email by remember {
        mutableStateOf("Aseem@gmail.com")
    }

    var username by remember {
        mutableStateOf("@aseemdev")
    }

    var profileImageUri by remember {
        mutableStateOf<android.net.Uri?>(null)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            profileImageUri = uri
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ProfileBackground)
            .statusBarsPadding()
    ) {

        // Top Header
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
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = ProfileHeading,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Settings",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = ProfileHeading
            )
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(15.dp))

            // Profile Hero Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                Color.White,
                                Color(0xFFF0F5FF)
                            )
                        )
                    )
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {

                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .border(
                                    3.dp,
                                    Color(0xFFDCE8FF),
                                    CircleShape
                                ),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.profile_avatar,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .border(
                                    3.dp,
                                    Color(0xFFDCE8FF),
                                    CircleShape
                                ),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(33.dp)
                            .clip(CircleShape)
                            .background(ProfileBlue)
                            .border(
                                2.dp,
                                Color.White,
                                CircleShape
                            )
                            .clickable {
                                imagePicker.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change profile picture",
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                Text(
                    text = fullName.ifBlank { "Your Name" },
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileHeading
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = ProfileSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Information Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileSectionIcon(
                        icon = Icons.Default.Badge
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Personal Information",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ProfileHeading
                        )

                        Text(
                            text = "Manage your identity across HealthMate",
                            fontSize = 11.sp,
                            color = ProfileSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(23.dp))

                // Full Name
                ProfileFieldLabel("Full Name")

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        validationMessage = null
                    },
                    placeholder = "Enter your full name",
                    icon = Icons.Default.PersonOutline,
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                ProfileFieldLabel("Email Address")

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = email,
                    onValueChange = {
                        email = it
                        validationMessage = null
                    },
                    placeholder = "Enter your email",
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username
                ProfileFieldLabel("Username")

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = username,
                    onValueChange = {
                        username = it
                        validationMessage = null
                    },
                    placeholder = "Enter username",
                    icon = Icons.Default.AlternateEmail,
                    keyboardType = KeyboardType.Text
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileSectionIcon(
                        icon = Icons.Default.AccountBox
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Account Information",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ProfileHeading
                    )
                }

                Spacer(modifier = Modifier.height(19.dp))

                // Member Since
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(ProfileFieldBackground)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProfileSectionIcon(
                        icon = Icons.Default.CalendarMonth,
                        size = 40.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Member Since",
                            fontSize = 11.sp,
                            color = ProfileSecondary
                        )

                        Text(
                            text = "October 2023",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = ProfileHeading
                        )
                    }

                    Text(
                        text = "7 min ago",
                        fontSize = 10.sp,
                        color = ProfileHeading
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                // Account Status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(ProfileFieldBackground)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProfileSectionIcon(
                        icon = Icons.Default.Shield,
                        size = 40.dp,
                        background = Color(0xFFD7E6FA),
                        tint = Color(0xFF60748C)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Account Status",
                            fontSize = 11.sp,
                            color = ProfileSecondary
                        )

                        Text(
                            text = "Standard Protected",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = ProfileHeading
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFE3EEFF))
                            .padding(
                                horizontal = 11.dp,
                                vertical = 7.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(ProfileBlue, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Active",
                            fontSize = 11.sp,
                            color = ProfileHeading
                        )
                    }
                }
            }

            validationMessage?.let { message ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Spacing after Account Status
            Spacer(modifier = Modifier.height(22.dp))

            // Validation Message
            validationMessage?.let { message ->
                Text(
                    text = message,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Save Changes Button
            Button(
                onClick = {
                    when {
                        fullName.isBlank() -> {
                            validationMessage =
                                "Please enter your full name."
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                            validationMessage =
                                "Please enter a valid email address."
                        }

                        username.isBlank() -> {
                            validationMessage =
                                "Please enter a username."
                        }

                        else -> {
                            onSaveChanges(
                                EditProfileUiModel(
                                    fullName = fullName.trim(),
                                    email = email.trim(),
                                    username = username.trim(),
                                    profileImageUri = profileImageUri
                                )
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0752CC)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = "Save Changes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cancel Button
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp),
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFF0F4FC)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    ProfileBorder
                )
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProfileHeading
                )
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Fixed Bottom Actions

    }
}

@Composable
private fun ProfileSectionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    size: androidx.compose.ui.unit.Dp = 36.dp,
    background: Color = Color(0xFFE8F0FF),
    tint: Color = ProfileBlue
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size * 0.48f)
        )
    }
}

@Composable
private fun ProfileFieldLabel(
    text: String
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = ProfileSecondary
    )
}

@Composable
private fun ProfileInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(53.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(ProfileFieldBackground)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ProfileBlue,
            modifier = Modifier.size(19.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = ProfileHeading
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            color = Color(0xFF91A0B7)
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}
