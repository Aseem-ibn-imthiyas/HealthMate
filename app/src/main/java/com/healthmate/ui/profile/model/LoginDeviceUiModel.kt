
package com.healthmate.ui.profile.model

data class LoginDeviceUiModel(
    val id: Int,
    val deviceName: String,
    val deviceDetails: String,
    val lastActive: String,
    val location: String,
    val isCurrentDevice: Boolean
)
