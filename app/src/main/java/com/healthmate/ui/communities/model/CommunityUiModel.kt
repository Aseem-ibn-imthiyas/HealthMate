package com.healthmate.ui.communities.model

data class CommunityUiModel(
    val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val imageRes: Int,
    val badge: String,
    val badgeIcon: String,
    val imageCaption: String,
    val memberCount: String,
    val status: String,
    val isTrending: Boolean = false
)
