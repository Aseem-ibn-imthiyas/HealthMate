
package com.healthmate.viewmodel

import com.healthmate.data.local.entity.HealthRecord

data class HealthRecordUiState(

    val records: List<HealthRecord> = emptyList(),

    val todayRecord: HealthRecord? = null,

    val searchResults: List<HealthRecord> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val successMessage: String? = null,

    val totalRecords: Int = 0
)
