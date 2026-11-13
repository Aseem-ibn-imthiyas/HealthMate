
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
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

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
    initialFullName: String = "Mohamed Aseem",
    initialEmail: String = "Aseem@gmail.com",
    initialProfileImageUri: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var fullName by remember(initialFullName) {
        mutableStateOf(initialFullName)
    }

    var email by remember(initialEmail) {
        mutableStateOf(initialEmail)
    }

    var username by remember {
        mutableStateOf("@aseemdev")
    }

    var profileImageUri by remember(initialProfileImageUri) {
        mutableStateOf(
            initialProfileImageUri?.let {
                android.net.Uri.parse(it)
            }
        )
    }

    var validationMessageResId by remember {
        mutableIntStateOf(0)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                profileImageUri = uri

            } catch (e: SecurityException) {
                validationMessageResId = R.string.edit_profile_image_error
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    contentDescription = stringResource(R.string.edit_profile_back),
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
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
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
                            contentDescription = stringResource(R.string.edit_profile_picture),
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
                            contentDescription = stringResource(R.string.edit_profile_picture),
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
                            .background(MaterialTheme.colorScheme.primary)
                            .border(
                                2.dp,
                                Color.White,
                                CircleShape
                            )
                            .clickable {
                                imagePicker.launch(arrayOf("image/*"))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = stringResource(R.string.edit_profile_change_picture),
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                Text(
                    text = fullName.ifBlank {
                        stringResource(R.string.edit_profile_your_name)
                    },
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Information Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surface)
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
                            text = stringResource(R.string.edit_profile_personal_information),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = stringResource(R.string.edit_profile_manage_identity),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(23.dp))

                // Full Name
                ProfileFieldLabel(
            stringResource(R.string.edit_profile_full_name)
                )

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        validationMessageResId = 0
                    },
                    placeholder = stringResource(R.string.edit_profile_enter_full_name),
                    icon = Icons.Default.PersonOutline,
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                ProfileFieldLabel(
            stringResource(R.string.edit_profile_email_address)
                )

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = email,
                    onValueChange = {
                        email = it
                        validationMessageResId = 0
                    },
                    placeholder = stringResource(R.string.edit_profile_enter_email),
                    icon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username
                ProfileFieldLabel(
                    stringResource(R.string.edit_profile_username)
                )

                Spacer(modifier = Modifier.height(7.dp))

                ProfileInputField(
                    value = username,
                    onValueChange = {
                        username = it
                        validationMessageResId = 0
                    },
                    placeholder = stringResource(R.string.edit_profile_enter_username),
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
                    .background(MaterialTheme.colorScheme.surface)
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
                        text = stringResource(R.string.edit_profile_account_information),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(19.dp))

                // Member Since
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            text = stringResource(R.string.edit_profile_member_since),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = stringResource(R.string.edit_profile_member_date),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = stringResource(R.string.edit_profile_time_ago),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                // Account Status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ProfileSectionIcon(
                        icon = Icons.Default.Shield,
                        size = 40.dp,
                        background = MaterialTheme.colorScheme.surfaceVariant,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.edit_profile_account_status),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = stringResource(R.string.edit_profile_standard_protected),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(
                                horizontal = 11.dp,
                                vertical = 7.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = stringResource(R.string.edit_profile_active),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            if (validationMessageResId != 0) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(validationMessageResId),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Spacing after Account Status
            Spacer(modifier = Modifier.height(22.dp))

            if (validationMessageResId != 0) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(validationMessageResId),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Save Changes Button
            Button(
                onClick = {
                    when {
                        fullName.isBlank() -> {
                            validationMessageResId = R.string.edit_profile_name_required
                        }

                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> {
                            validationMessageResId = R.string.edit_profile_valid_email
                        }

                        username.isBlank() -> {
                            validationMessageResId = R.string.edit_profile_username_required
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
                    containerColor = MaterialTheme.colorScheme.primary
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
                    text = stringResource(R.string.edit_profile_save_changes),
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
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                )
            ) {
                Text(
                    text = stringResource(R.string.settings_cancel),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
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
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    tint: Color = MaterialTheme.colorScheme.primary
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
        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
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
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    innerTextField()
                }
            }
        )
    }
}
