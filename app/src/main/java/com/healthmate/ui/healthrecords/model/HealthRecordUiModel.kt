
package com.healthmate.ui.healthrecords.model

data class HealthRecordUiModel(
    val id: Int,
    val date: String,
    val dateLabel: String,
    val steps: Int,
    val calories: Int,
    val water: Int,
    val updatedAt: String,
    val dateIso: String = ""

)
