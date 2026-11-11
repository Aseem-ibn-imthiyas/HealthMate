
package com.healthmate.ui.communities

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.R
import com.healthmate.ui.communities.model.CommunityUiModel

private val CommunityBackground = Color(0xFFF7F8FF)
private val CommunityBlue = Color(0xFF0755D9)
private val CommunityHeading = Color(0xFF102641)
private val CommunitySecondary = Color(0xFF454C60)

private data class CommunityCategory(
    val name: String,
    val icon: ImageVector
)

private val communityCategories = listOf(
    CommunityCategory("Fitness", Icons.Default.FitnessCenter),
    CommunityCategory("Nutrition", Icons.Default.Spa),
    CommunityCategory("Mindfulness", Icons.Default.SelfImprovement),
    CommunityCategory("Wellness", Icons.Default.Favorite),
    CommunityCategory("Lifestyle", Icons.Default.SentimentSatisfied)
)

@Composable
fun CommunitiesScreen(
    onCreateCommunityClick: () -> Unit = {},
    onViewAllCategoriesClick: () -> Unit = {},
    onCommunityClick: (CommunityUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var selectedCategory by remember {
        mutableStateOf<String?>(null)
    }

    val communities = remember {
        listOf(
            CommunityUiModel(
                id = 1,
                name = "Daily Fitness Club",
                category = "Fitness",
                description = "Building active habits and celebrating daily steps together with weekly leaderboard challenges and",
                imageRes = R.drawable.community_fitness,
                badge = "Trending",
                badgeIcon = "🔥",
                imageCaption = "Weekly Milestone: 100k Steps",
                memberCount = "12.4k members",
                status = "Rank #1",
                isTrending = true
            ),
            CommunityUiModel(
                id = 2,
                name = "Healthy Eating Circle",
                category = "Nutrition",
                description = "Sharing wholesome recipes, clean meal-prep ideas, macro-friendly tips, and dietitian-approved",
                imageRes = R.drawable.community_nutrition,
                badge = "Nutrition",
                badgeIcon = "◉",
                imageCaption = "New recipes posted daily",
                memberCount = "8.7k members",
                status = "Active Now"
            ),
            CommunityUiModel(
                id = 3,
                name = "Mindful Living",
                category = "Mindfulness",
                description = "Daily breathwork routines, stress reduction practices, gratitude journals, and live guided...",
                imageRes = R.drawable.community_mindfulness,
                badge = "Daily Zen",
                badgeIcon = "♧",
                imageCaption = "Live Meditation @ 8:00 AM",
                memberCount = "6.2k members",
                status = "Guided"
            )
        )
    }

    val filteredCommunities = communities.filter {
        selectedCategory == null || it.category == selectedCategory
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommunityBackground)
            .statusBarsPadding()
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 27.dp,
                    bottom = 25.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Communities",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CommunityHeading
                )

                Text(
                    text = "Connect, share and grow healthier together.",
                    fontSize = 11.sp,
                    color = CommunitySecondary
                )
            }

            IconButton(
                onClick = onCreateCommunityClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(CommunityBlue, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Community",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        // Category Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 13.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Explore Categories",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = CommunityHeading
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "View all",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = CommunityBlue,
                modifier = Modifier.clickable {
                    onViewAllCategoriesClick()
                }
            )
        }

        // Categories
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            items(communityCategories) { category ->

                CommunityCategoryItem(
                    category = category,
                    selected = selectedCategory == category.name,
                    onClick = {
                        selectedCategory =
                            if (selectedCategory == category.name) {
                                null
                            } else {
                                category.name
                            }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(39.dp))

        // Popular Communities Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 32.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedCategory == null) {
                    "Popular Communities"
                } else {
                    selectedCategory!!
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = CommunityHeading
            )
        }

        Spacer(modifier = Modifier.height(17.dp))

        // Community Cards
        if (filteredCommunities.isEmpty()) {
            EmptyCommunityState(
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = filteredCommunities,
                    key = { it.id }
                ) { community ->

                    CommunityCard(
                        community = community,
                        onClick = {
                            onCommunityClick(community)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunityCategoryItem(
    category: CommunityCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) {
        Color(0xFF0755D9)
    } else {
        Color(0xFFE7EEFD)
    }

    val tint = if (selected) {
        Color.White
    } else {
        Color(0xFF315B9B)
    }

    Column(
        modifier = Modifier
            .width(56.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = CommunityHeading,
            maxLines = 1
        )
    }
}

@Composable
private fun CommunityCard(
    community: CommunityUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(19.dp))
            .background(Color.White)
    ) {

        // Cover Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {

            androidx.compose.foundation.Image(
                painter = painterResource(
                    id = community.imageRes
                ),
                contentDescription = community.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Image overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Color.Black.copy(alpha = 0.38f)
                    )
            )

            // Category badge
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(
                        horizontal = 11.dp,
                        vertical = 6.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = community.badgeIcon,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = community.badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CommunityHeading
                )
            }

            // Image Caption
            Text(
                text = community.imageCaption,
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = 12.dp,
                        bottom = 11.dp
                    )
            )
        }

        // Community Information
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 15.dp
            )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = community.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CommunityHeading,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE7F0FF))
                        .padding(
                            horizontal = 9.dp,
                            vertical = 4.dp
                        )
                ) {
                    Text(
                        text = community.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = CommunityBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = community.description,
                fontSize = 13.sp,
                lineHeight = 22.sp,
                color = CommunitySecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Members and View Community
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = CommunityBlue,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = community.memberCount,
                    fontSize = 11.sp,
                    color = CommunitySecondary,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    onClick = onClick,
                    shape = CircleShape,
                    color = Color(0xFFD9E7FA)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 15.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Community",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CommunityHeading
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = CommunityHeading,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCommunityState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null,
            tint = Color(0xFF91A9D1),
            modifier = Modifier.size(55.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "No Communities Found",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = CommunityHeading
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Try exploring another category.",
            fontSize = 12.sp,
            color = CommunitySecondary
        )
    }
}
