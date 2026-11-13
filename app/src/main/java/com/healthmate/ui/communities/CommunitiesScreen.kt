
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
import androidx.compose.ui.res.stringResource

private val CommunityBackground = Color(0xFFF7F8FF)
private val CommunityBlue = Color(0xFF0755D9)
private val CommunityHeading = Color(0xFF102641)
private val CommunitySecondary = Color(0xFF454C60)

private data class CommunityCategory(
    val key: String,
    val name: String,
    val icon: ImageVector
)

@Composable
private fun getCommunityCategories() = listOf(
    CommunityCategory(
        "Fitness",
        stringResource(R.string.communities_fitness),
        Icons.Default.FitnessCenter
    ),
    CommunityCategory(
        "Nutrition",
        stringResource(R.string.communities_nutrition),
        Icons.Default.Spa
    ),
    CommunityCategory(
        "Mindfulness",
        stringResource(R.string.communities_mindfulness),
        Icons.Default.SelfImprovement
    ),
    CommunityCategory(
        "Wellness",
        stringResource(R.string.communities_wellness),
        Icons.Default.Favorite
    ),
    CommunityCategory(
        "Lifestyle",
        stringResource(R.string.communities_lifestyle),
        Icons.Default.SentimentSatisfied
    )
)

@Composable
fun CommunitiesScreen(
    communities: List<CommunityUiModel> = emptyList(),
    onCreateCommunityClick: () -> Unit = {},
    onViewAllCategoriesClick: () -> Unit = {},
    onCommunityClick: (CommunityUiModel) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var selectedCategory by remember {
        mutableStateOf<String?>(null)
    }

    val communityCategories = getCommunityCategories()

    // All three hardcoded CommunityUiModel entries

    val filteredCommunities = communities.filter {
        selectedCategory == null || it.category == selectedCategory
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
                    text = stringResource(R.string.communities_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(R.string.communities_subtitle),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onCreateCommunityClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.communities_create),
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
                text = stringResource(R.string.communities_explore_categories),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.communities_view_all),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
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
                    selected = selectedCategory == category.key,
                    onClick = {
                        selectedCategory =
                            if (selectedCategory == category.key) {
                                null
                            } else {
                                category.key
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
                    stringResource(R.string.communities_popular)
                } else {
                    communityCategories.firstOrNull {
                        it.key == selectedCategory
                    }?.name ?: selectedCategory!!
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
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
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val tint = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
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
            color = MaterialTheme.colorScheme.onBackground,
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
            .background(MaterialTheme.colorScheme.surface)
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
                    .background(MaterialTheme.colorScheme.surface)
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
                    color = MaterialTheme.colorScheme.onBackground
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
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(
                            horizontal = 9.dp,
                            vertical = 4.dp
                        )
                ) {
                    Text(
                        text = community.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = community.description,
                fontSize = 13.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = community.memberCount,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    onClick = onClick,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 15.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.communities_view_community),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
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
            text = stringResource(R.string.communities_empty_title),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.communities_empty_description),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
