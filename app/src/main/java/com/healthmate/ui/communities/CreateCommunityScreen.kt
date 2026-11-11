
package com.healthmate.ui.communities

import android.net.Uri
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.healthmate.ui.communities.model.CreateCommunityUiModel

private val CreateBlue = Color(0xFF2563EB)
private val CreateDarkBlue = Color(0xFF1D447E)
private val CreateHeading = Color(0xFF142944)
private val CreateSecondary = Color(0xFF53647E)
private val CreateBackground = Color(0xFFF7F8FF)
private val CreateFieldBackground = Color(0xFFEEF4FE)
private val CreateBorder = Color(0xFFE1E9F5)

private val createCategories = listOf(
    "Fitness",
    "Nutrition",
    "Mindfulness",
    "Wellness",
    "Lifestyle"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCommunityScreen(
    onBackClick: () -> Unit,
    onCancelClick: () -> Unit,
    onCreateCommunity: (CreateCommunityUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var coverImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var communityName by remember {
        mutableStateOf("Sunrise Trail Runners")
    }

    var selectedCategory by remember {
        mutableStateOf("Fitness")
    }

    var categoryDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var description by remember {
        mutableStateOf(
            "We are an early morning trail running group committed to steady pace progression, injury prevention, and mutual motivation across all skill levels."
        )
    }

    var privacy by remember {
        mutableStateOf("Public")
    }

    var guidelines by remember {
        mutableStateOf(
            "Be supportive of all paces. Check in with hydration buddies before 10k workouts."
        )
    }

    var guidelinesExpanded by remember {
        mutableStateOf(true)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                coverImageUri = uri
            }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreateBackground)
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
                    tint = CreateHeading,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Communities",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = CreateHeading
            )
        }

        // Scrollable Form
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Build Your Community",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = CreateDarkBlue
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Bring people together, share your health journey, and motivate each other to achieve your goals.",
                fontSize = 13.sp,
                lineHeight = 22.sp,
                color = CreateSecondary
            )

            Spacer(modifier = Modifier.height(19.dp))

            // Cover Image Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormSectionLabel(
                    text = "Upload Cover Image",
                    required = true
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Recommended: 16:9 ratio",
                    fontSize = 10.sp,
                    color = CreateSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Cover Image Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        CreateBorder,
                        RoundedCornerShape(17.dp)
                    )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {

                    if (coverImageUri != null) {
                        AsyncImage(
                            model = coverImageUri,
                            contentDescription = "Community cover image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        // Replace with your exported Figma image
                        AsyncImage(
                            model = com.healthmate.R.drawable.community_fitness,
                            contentDescription = "Default community cover",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }

                    // Image Select Button
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Choose cover image",
                            tint = CreateBlue,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Selected Image / Replace
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.30f)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 9.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (coverImageUri != null) {
                            Text(
                                text = "Image selected",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Text(
                                text = "trail_runners.jpg",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Button(
                            onClick = {
                                imagePickerLauncher.launch("image/*")
                            },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp
                            ),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CreateBlue
                            )
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = if (coverImageUri == null) {
                                    "Replace"
                                } else {
                                    "Change"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = CreateBlue,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Choose an image that represents your community",
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                        color = CreateSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CreateFieldBackground)
                            .padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            )
                    ) {
                        Text(
                            text = "PNG, JPG ≤5MB",
                            fontSize = 9.sp,
                            color = CreateSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            // Community Information Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {

                // Community Name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormSectionLabel(
                        text = "Community Name",
                        required = true
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${communityName.length} / 50",
                        fontSize = 11.sp,
                        color = CreateSecondary
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                FormTextInput(
                    value = communityName,
                    onValueChange = {
                        if (it.length <= 50) {
                            communityName = it
                            validationMessage = null
                        }
                    },
                    placeholder = "Enter community name",
                    leadingIcon = {
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            tint = CreateBlue
                        )
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Community Category
                FormSectionLabel(
                    text = "Community Category",
                    required = true
                )

                Spacer(modifier = Modifier.height(9.dp))

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(CreateFieldBackground)
                            .clickable {
                                categoryDropdownExpanded = true
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FormIcon(
                            icon = Icons.Default.Category
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = when (selectedCategory) {
                                "Fitness" -> "Fitness & Endurance Training"
                                "Nutrition" -> "Nutrition & Healthy Eating"
                                "Mindfulness" -> "Mindfulness & Meditation"
                                "Wellness" -> "Health & Wellness"
                                else -> "Lifestyle & Community"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = CreateHeading,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select category",
                            tint = CreateSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = {
                            categoryDropdownExpanded = false
                        }
                    ) {
                        createCategories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(category)
                                },
                                onClick = {
                                    selectedCategory = category
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(9.dp))

                // Category Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    createCategories.take(4).forEach { category ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(
                                    if (selectedCategory == category) {
                                        Color(0xFFDCEAFF)
                                    } else {
                                        Color(0xFFF0F5FD)
                                    }
                                )
                                .clickable {
                                    selectedCategory = category
                                }
                                .padding(
                                    horizontal = 5.dp,
                                    vertical = 8.dp
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedCategory == category) {
                                    "✓ $category"
                                } else {
                                    category
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (selectedCategory == category) {
                                    CreateBlue
                                } else {
                                    CreateSecondary
                                },
                                maxLines = 1,
                                overflow = TextOverflow.Clip
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Description
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormSectionLabel(
                        text = "Community Description",
                        required = true
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${description.length} / 300",
                        fontSize = 11.sp,
                        color = CreateSecondary
                    )
                }

                Spacer(modifier = Modifier.height(9.dp))

                BasicTextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= 300) {
                            description = it
                            validationMessage = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(CreateFieldBackground)
                        .padding(15.dp),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 22.sp,
                        color = CreateHeading
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (description.isEmpty()) {
                                Text(
                                    text = "Describe your community...",
                                    fontSize = 13.sp,
                                    color = Color(0xFF8B9BB3)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Community Privacy
            Text(
                text = "Community Privacy",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = CreateDarkBlue
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Choose who can find, discover, and join this group.",
                fontSize = 13.sp,
                color = CreateSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrivacyOptionCard(
                title = "Public Community",
                description = "Anyone can discover and join this community.",
                selected = privacy == "Public",
                isPublic = true,
                onClick = {
                    privacy = "Public"
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PrivacyOptionCard(
                title = "Private Community",
                description = "Only invited members can join.",
                selected = privacy == "Private",
                isPublic = false,
                onClick = {
                    privacy = "Private"
                }
            )

            Spacer(modifier = Modifier.height(17.dp))

            // Community Guidelines
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            guidelinesExpanded = !guidelinesExpanded
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormIcon(
                        icon = Icons.Default.VerifiedUser
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Community Guidelines",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CreateDarkBlue
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Optional",
                                fontSize = 9.sp,
                                color = CreateSecondary,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(CreateFieldBackground)
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )
                            )
                        }

                        Text(
                            text = "Help members understand how to participate respectfully.",
                            fontSize = 10.sp,
                            lineHeight = 16.sp,
                            color = CreateSecondary
                        )
                    }

                    Icon(
                        imageVector = if (guidelinesExpanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "Toggle guidelines",
                        tint = CreateSecondary
                    )
                }

                if (guidelinesExpanded) {
                    Spacer(modifier = Modifier.height(15.dp))

                    BasicTextField(
                        value = guidelines,
                        onValueChange = {
                            guidelines = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(94.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(CreateFieldBackground)
                            .padding(14.dp),
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 21.sp,
                            color = CreateHeading
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                if (guidelines.isEmpty()) {
                                    Text(
                                        text = "Write community guidelines...",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8B9BB3)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }


            validationMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error
                )
            }

// Spacing before action buttons
            Spacer(modifier = Modifier.height(24.dp))

// Scrollable Bottom Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(
                        horizontal = 14.dp,
                        vertical = 16.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // Cancel Button
                    OutlinedButton(
                        onClick = onCancelClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(49.dp),
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFD8E6FF)
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CreateDarkBlue
                        )
                    }

                    // Create Community Button
                    Button(
                        onClick = {
                            when {
                                coverImageUri == null -> {
                                    validationMessage =
                                        "Please upload a community cover image."
                                }

                                communityName.isBlank() -> {
                                    validationMessage =
                                        "Please enter a community name."
                                }

                                description.isBlank() -> {
                                    validationMessage =
                                        "Please enter a community description."
                                }

                                else -> {
                                    onCreateCommunity(
                                        CreateCommunityUiModel(
                                            name = communityName.trim(),
                                            category = selectedCategory,
                                            description = description.trim(),
                                            coverImageUri = coverImageUri,
                                            privacy = privacy,
                                            guidelines = guidelines.trim()
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(49.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CreateBlue
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Create Community",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = CreateSecondary,
                        modifier = Modifier.size(13.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "You can edit privacy and rules anytime in Settings",
                        fontSize = 10.sp,
                        color = CreateSecondary
                    )
                }
            }

// Bottom spacing
            Spacer(modifier = Modifier.height(24.dp))

        }


    }
}

@Composable
private fun FormSectionLabel(
    text: String,
    required: Boolean = false
) {
    Row {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CreateDarkBlue
        )

        if (required) {
            Text(
                text = " *",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }
    }
}

@Composable
private fun FormIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                Color(0xFFDDEAFF),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CreateBlue,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun FormTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(CreateFieldBackground)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FormIcon(
            icon = Icons.Default.Groups
        )

        Spacer(modifier = Modifier.width(10.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 13.sp,
                color = CreateHeading
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 13.sp,
                            color = Color(0xFF8B9BB3)
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (value.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "Clear name",
                tint = Color(0xFF8B9BB3),
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        onValueChange("")
                    }
            )
        }
    }
}

@Composable
private fun PrivacyOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    isPublic: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) {
        CreateBlue
    } else {
        CreateBorder
    }

    val background = if (selected) {
        Color(0xFFF0F5FF)
    } else {
        Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(background)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (selected) CreateBlue else Color(0xFFEEF4FD),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isPublic) {
                    Icons.Default.Public
                } else {
                    Icons.Default.Lock
                },
                contentDescription = null,
                tint = if (selected) {
                    Color.White
                } else {
                    Color(0xFF7186A6)
                },
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) CreateDarkBlue else CreateHeading
                )

                if (isPublic) {
                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "Default",
                        fontSize = 9.sp,
                        color = CreateBlue,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFDCEAFF))
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 11.sp,
                lineHeight = 17.sp,
                color = CreateSecondary
            )
        }

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    if (selected) CreateBlue else Color(0xFFE1E7F0),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
