
package com.healthmate.ui.communities

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.healthmate.R
import com.healthmate.ui.communities.model.CommunityPostUiModel
import com.healthmate.ui.communities.model.CommunityUiModel
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import com.healthmate.ui.communities.EditCommunityPostScreen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

private val FeedBackground = Color(0xFFF7F8FF)
private val FeedBlue = Color(0xFF0755D9)
private val FeedHeading = Color(0xFF102641)
private val FeedSecondary = Color(0xFF53647E)
private val FeedLightBlue = Color(0xFFE8F0FF)

private val samplePosts = listOf(
    CommunityPostUiModel(
        id = 1,
        authorName = "Sarah Jenkins",
        authorInitials = "SJ",
        timeAgo = "15m ago",
        location = "Morning Trail",
        content = "Crushed my morning 7,000 steps trail jog before work! The morning sun was unbelievable. Remember to stay hydrated everyone! 🏃‍♀️",
        activitySummary = "Activity: 7,240 steps • 45 min • 380 kcal",
        imageRes = R.drawable.post_sarah_image,
        likes = 142,
        comments = 28,
        shares = 12
    ),
    CommunityPostUiModel(
        id = 2,
        authorName = "Elena Rostova",
        authorInitials = "ER",
        timeAgo = "3h ago",
        location = "Recovery",
        content = "Rest day stretches and mindful breathing done. Consistency over intensity every single time! 🧘",
        activitySummary = "Mindful Session: 20 min • HRV 68ms",
        likes = 64,
        comments = 9,
        shares = 2
    )
)

@Composable
fun ViewCommunityScreen(
    onEditPost: (Int, String, String?) -> Unit = { _, _, _ -> },
    onDeletePost: (Int) -> Unit = {},
    userName: String = "User",
    userProfileImageUri: String? = null,
    communityPosts: List<CommunityPostUiModel> = emptyList(),
    onLikePostClick: (Int) -> Unit = {},
    memberCount: Int = 0,
    community: CommunityUiModel,
    onBackClick: () -> Unit,
    onPhotoClick: () -> Unit = {},
    onSharePost: (String, String?) -> Unit,
    onCommentClick: (CommunityPostUiModel) -> Unit = {},
    onSharePostClick: (CommunityPostUiModel) -> Unit = {},
    onNotificationChanged: (Boolean) -> Unit = {},
    isJoined: Boolean = false,
    onJoinCommunityClick: () -> Unit = {},
    onLeaveCommunityClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    currentUserId: Int = 0
) {


    var notificationsEnabled by remember {
        mutableStateOf(false)
    }

    var postText by remember {
        mutableStateOf("")
    }

    var selectedSort by remember {
        mutableStateOf("Most Recent")
    }

    var sortExpanded by remember {
        mutableStateOf(false)
    }

    var attachedPhoto by remember {
        mutableStateOf<android.net.Uri?>(null)
    }

    var selectedPostToEdit by remember {
        mutableStateOf<CommunityPostUiModel?>(null)
    }

    var selectedPostToDelete by remember {
        mutableStateOf<CommunityPostUiModel?>(null)
    }

    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                attachedPhoto = uri

            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FeedBackground)
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
                    tint = FeedHeading,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Communities",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = FeedHeading
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 32.dp,
                end = 32.dp,
                top = 14.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Community Header
            item {
                CommunityHeroCard(
                    memberCount = memberCount,
                    community = community,
                    isJoined = isJoined,
                    notificationsEnabled = notificationsEnabled,
                    onJoinClick = {
                        if (isJoined) {
                            onLeaveCommunityClick()
                        } else {
                            onJoinCommunityClick()
                        }
                    },
                    onNotificationClick = {
                        notificationsEnabled = !notificationsEnabled
                        onNotificationChanged(notificationsEnabled)
                    },
                )
            }

            // Post Composer
            item {
                CommunityPostComposer(
                    postText = postText,
                    onPostTextChange = {
                        postText = it
                    },
                    attachedPhoto = attachedPhoto,
                    userName = userName,
                    userProfileImageUri = userProfileImageUri,
                    onPhotoClick = {
                        photoPicker.launch(arrayOf("image/*"))
                        onPhotoClick()
                    },
                    onGoalClick = {
                        postText = if (postText.isBlank()) {
                            "I completed my 10k goal today! 💪"
                        } else {
                            "$postText #10kGoal"
                        }
                    },
                    onShareClick = {
                        if (postText.isNotBlank() || attachedPhoto != null) {

                            onSharePost(
                                postText,
                                attachedPhoto?.toString()
                            )

                            postText = ""
                            attachedPhoto = null
                        }
                    }
                )
            }

            // Feed Heading
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Community Feed",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = FeedHeading,
                        modifier = Modifier.weight(1f)
                    )

                    Box {
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(FeedLightBlue)
                                .clickable {
                                    sortExpanded = true
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 9.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedSort,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = FeedSecondary
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Sort feed",
                                tint = FeedSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = {
                                sortExpanded = false
                            }
                        ) {
                            listOf(
                                "Most Recent",
                                "Most Popular",
                                "Most Liked"
                            ).forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(option)
                                    },
                                    onClick = {
                                        selectedSort = option
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Feed Posts
            items(
                items = communityPosts,
                key = { it.id }
            ) { post ->
                CommunityFeedPost(
                    post = post,
                    onLikeClick = {
                        onLikePostClick(post.id)
                    },
                    currentUserId = currentUserId,
                    onCommentClick = {
                        onCommentClick(post)
                    },
                    onShareClick = {
                        onSharePostClick(post)
                    },
                    onEditClick = {
                        selectedPostToEdit = post
                    },
                    onDeleteClick = {
                        selectedPostToDelete = post
                    }

                )
            }
        }
    }

    selectedPostToEdit?.let { post ->

        EditCommunityPostScreen(
            initialContent = post.content,
            initialImageUri = post.postImageUri,

            onDismiss = {
                selectedPostToEdit = null
            },

            onSavePost = { updatedContent, updatedImageUri ->

                onEditPost(
                    post.id,
                    updatedContent,
                    updatedImageUri
                )

                selectedPostToEdit = null
            }
        )
    }

    selectedPostToDelete?.let { post ->
        AlertDialog(
            onDismissRequest = {
                selectedPostToDelete = null
            },
            title = {
                Text(
                    text = "Delete Post?",
                    color = FeedHeading
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this post? This action cannot be undone.",
                    color = FeedSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeletePost(post.id)
                        selectedPostToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = Color(0xFFE04444)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedPostToDelete = null
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
private fun CommunityHeroCard(
    community: CommunityUiModel,
    isJoined: Boolean,
    memberCount: Int = 0,
    notificationsEnabled: Boolean,
    onJoinClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(27.dp))
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {
            Image(
                painter = painterResource(community.imageRes),
                contentDescription = community.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.42f))
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = null,
                    tint = FeedBlue,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Verified Club",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = FeedHeading
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(FeedBlue)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "RANK #1 FITNESS",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = community.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 14.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = community.description,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = FeedSecondary
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = null,
                    tint = FeedBlue,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = memberCount.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = FeedHeading
                    )

                    Text(
                        text = "Total Members",
                        fontSize = 10.sp,
                        color = FeedSecondary
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFEAF1FC))
                        .padding(
                            horizontal = 11.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(FeedBlue, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Community Members",
                        fontSize = 10.sp,
                        color = FeedHeading
                    )
                }
            }

            Spacer(modifier = Modifier.height(17.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD9E7FA)
                    )
                ) {
                    Icon(
                        if (isJoined) Icons.Default.CheckCircle
                        else Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = FeedBlue,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = if (isJoined) "Joined" else "Join Community",
                        color = FeedBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(FeedLightBlue, CircleShape)
                ) {
                    Icon(
                        if (notificationsEnabled) {
                            Icons.Default.NotificationsActive
                        } else {
                            Icons.Default.NotificationsNone
                        },
                        contentDescription = "Toggle notifications",
                        tint = FeedSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityPostComposer(
    userName: String = "User",
    userProfileImageUri: String? = null,
    postText: String,
    onPostTextChange: (String) -> Unit,
    attachedPhoto: android.net.Uri?,
    onPhotoClick: () -> Unit,
    onGoalClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(27.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!userProfileImageUri.isNullOrBlank()) {
                AsyncImage(
                    model = userProfileImageUri,
                    contentDescription = "User profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.profile_avatar),
                    contentDescription = "User profile",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(11.dp))

            Column {
                Text(
                    text = userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FeedHeading
                )

                Text(
                    text = "Ready to inspire today",
                    fontSize = 10.sp,
                    color = FeedSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = postText,
            onValueChange = onPostTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFEEF3FF))
                .padding(14.dp),
            textStyle = TextStyle(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = FeedHeading
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (postText.isEmpty()) {
                        Text(
                            text = "What progress would you like to share today?",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = FeedSecondary
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (attachedPhoto != null) {
            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = attachedPhoto,
                contentDescription = "Attached photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(FeedLightBlue)
                    .clickable(onClick = onPhotoClick)
                    .padding(horizontal = 11.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = FeedBlue,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Photo",
                    fontSize = 11.sp,
                    color = FeedSecondary
                )
            }

            Spacer(modifier = Modifier.width(7.dp))

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(FeedLightBlue)
                    .clickable(onClick = onGoalClick)
                    .padding(horizontal = 11.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DirectionsWalk,
                    contentDescription = null,
                    tint = FeedBlue,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "10k Goal",
                    fontSize = 11.sp,
                    color = FeedSecondary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onShareClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AC8)
                ),
                contentPadding = PaddingValues(
                    horizontal = 19.dp,
                    vertical = 10.dp
                )
            ) {
                Text(
                    text = "Share",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

@Composable
private fun CommunityFeedPost(
    post: CommunityPostUiModel,
    currentUserId: Int,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showPostOptions by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(29.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFD9E7FA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (!post.authorProfileImageUri.isNullOrBlank()) {
                    AsyncImage(
                        model = post.authorProfileImageUri,
                        contentDescription = "Author profile image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Text(
                        text = post.authorInitials,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF365D9B)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = post.authorName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FeedHeading
                )

                Text(
                    text = "${post.timeAgo} • ${post.location}",
                    fontSize = 11.sp,
                    color = FeedSecondary
                )
            }

            Box {
                IconButton(
                    onClick = {
                        showPostOptions = true
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        Icons.Default.MoreHoriz,
                        contentDescription = "More post options",
                        tint = FeedSecondary
                    )
                }

                DropdownMenu(
                    expanded = showPostOptions,
                    onDismissRequest = {
                        showPostOptions = false
                    }
                ) {
                    if (post.userId == currentUserId) {
                        DropdownMenuItem(
                            text = { Text("Edit Post") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                showPostOptions = false
                                onEditClick()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete Post") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                showPostOptions = false
                                onDeleteClick()
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text("Report Post") },
                            onClick = {
                                showPostOptions = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = post.content,
            fontSize = 13.sp,
            lineHeight = 22.sp,
            color = FeedHeading
        )

        if (!post.postImageUri.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))

            AsyncImage(
                model = post.postImageUri,
                contentDescription = "Community post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
        }

        if (post.activitySummary != null) {
            Spacer(modifier = Modifier.height(11.dp))

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFDCEAFF))
                    .padding(
                        horizontal = 11.dp,
                        vertical = 7.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = FeedBlue,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = post.activitySummary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FeedBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (post.imageRes != null) {
            Spacer(modifier = Modifier.height(15.dp))

            Image(
                painter = painterResource(post.imageRes),
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(207.dp)
                    .clip(RoundedCornerShape(17.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onLikeClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (post.isLiked) Icons.Default.Favorite
                    else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.isLiked) {
                        Color(0xFFD22222)
                    } else {
                        FeedSecondary
                    },
                    modifier = Modifier.size(21.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = post.likes.toString(),
                    fontSize = 12.sp,
                    color = FeedHeading
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onCommentClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = FeedSecondary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = post.comments.toString(),
                    fontSize = 12.sp,
                    color = FeedSecondary
                )
            }

            Row(
                modifier = Modifier.clickable(onClick = onShareClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = FeedSecondary,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = post.shares.toString(),
                    fontSize = 12.sp,
                    color = FeedSecondary
                )
            }
        }
    }
}
