
package com.healthmate.ui.communities.model

import android.net.Uri

data class CreateCommunityUiModel(
    val name: String,
    val category: String,
    val description: String,
    val coverImageUri: Uri?,
    val privacy: String,
    val guidelines: String
)
