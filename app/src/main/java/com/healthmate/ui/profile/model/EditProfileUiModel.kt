package com.healthmate.ui.profile.model

import android.net.Uri

data class EditProfileUiModel(
    val fullName: String,
    val email: String,
    val username: String,
    val profileImageUri: Uri? = null
)
