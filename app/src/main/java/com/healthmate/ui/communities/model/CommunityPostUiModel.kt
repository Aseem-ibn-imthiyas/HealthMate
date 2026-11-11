
package com.healthmate.ui.communities.model

data class CommunityPostUiModel(
    val id: Int,
    val authorName: String,
    val authorInitials: String,
    val timeAgo: String,
    val location: String,
    val content: String,
    val activitySummary: String? = null,
    val imageRes: Int? = null,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val isLiked: Boolean = false
)
