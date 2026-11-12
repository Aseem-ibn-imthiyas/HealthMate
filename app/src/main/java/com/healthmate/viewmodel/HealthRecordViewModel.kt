
package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.healthmate.data.local.entity.HealthRecord
import com.healthmate.data.repository.HealthRecordRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import kotlinx.coroutines.Job

class HealthRecordViewModel(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HealthRecordUiState(isLoading = true)
    )

    private var searchJob: Job? = null

    val uiState: StateFlow<HealthRecordUiState> =
        _uiState.asStateFlow()

    init {
        observeAllRecords()
        observeRecordCount()
        observeTodayRecord()
    }

    // ------------------------------------
    // READ ALL RECORDS
    // ------------------------------------

    private fun observeAllRecords() {
        viewModelScope.launch {
            repository.getAllRecords().collectLatest { records ->

                _uiState.value = _uiState.value.copy(
                    records = records,
                    isLoading = false
                )
            }
        }
    }

    // ------------------------------------
    // RECORD COUNT
    // ------------------------------------

    private fun observeRecordCount() {
        viewModelScope.launch {
            repository.getRecordCount().collectLatest { count ->

                _uiState.value = _uiState.value.copy(
                    totalRecords = count
                )
            }
        }
    }

    // ------------------------------------
    // TODAY'S HEALTH RECORD
    // ------------------------------------

    private fun observeTodayRecord() {
        viewModelScope.launch {

            val today = getTodayDate()

            repository.getTodayRecord(today)
                .collectLatest { record ->

                    _uiState.value = _uiState.value.copy(
                        todayRecord = record
                    )
                }
        }
    }

    // ------------------------------------
    // CREATE / UPDATE RECORD
    // ------------------------------------

    fun saveRecord(
        date: String,
        steps: Int,
        calories: Int,
        water: Int
    ) {

        if (!isValidDate(date)) {
            showError("Please enter a valid date (yyyy-MM-dd).")
            return
        }

        if (steps < 0 || calories < 0 || water < 0) {
            showError("Health values cannot be negative.")
            return
        }

        if (steps > 200000 || calories > 100000 || water > 100000) {
            showError("Please enter realistic health values.")
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            try {

                val record = HealthRecord(
                    date = date,
                    steps = steps,
                    calories = calories,
                    water = water
                )

                repository.saveRecord(record)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Health record saved successfully."
                )

            } catch (e: Exception) {

                showError(
                    e.message ?: "Unable to save health record."
                )
            }
        }
    }

    // ------------------------------------
    // UPDATE EXISTING RECORD
    // ------------------------------------

    fun updateRecord(
        record: HealthRecord
    ) {

        if (!isValidDate(record.date)) {
            showError("Please enter a valid date.")
            return
        }

        if (
            record.steps < 0 ||
            record.calories < 0 ||
            record.water < 0
        ) {
            showError("Health values cannot be negative.")
            return
        }

        viewModelScope.launch {

            try {

                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                val existingRecord =
                    repository.getRecordByDate(record.date)

                if (
                    existingRecord != null &&
                    existingRecord.id != record.id
                ) {
                    showError(
                        "A health record already exists for this date."
                    )
                    return@launch
                }

                repository.updateRecord(record)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "Health record updated successfully."
                )

            } catch (e: Exception) {

                showError(
                    e.message ?: "Unable to update health record."
                )
            }
        }
    }

    // ------------------------------------
    // DELETE RECORD
    // ------------------------------------

    fun deleteRecord(
        record: HealthRecord
    ) {
        viewModelScope.launch {

            try {

                repository.deleteRecord(record)

                _uiState.value = _uiState.value.copy(
                    successMessage = "Health record deleted successfully."
                )

            } catch (e: Exception) {

                showError(
                    e.message ?: "Unable to delete health record."
                )
            }
        }
    }

    // ------------------------------------
    // SEARCH RECORDS
    // ------------------------------------

    fun searchRecords(searchDate: String) {

        searchJob?.cancel()

        if (searchDate.isBlank()) {
            clearSearch()
            return
        }

        if (!isValidDate(searchDate)) {
            showError("Please enter a valid date (yyyy-MM-dd).")
            return
        }

        searchJob = viewModelScope.launch {

            try {
                repository.searchRecords(searchDate)
                    .collectLatest { results ->

                        _uiState.value = _uiState.value.copy(
                            searchResults = results,
                            errorMessage = null
                        )
                    }

            } catch (e: Exception) {

                showError(
                    e.message ?: "Unable to search records."
                )
            }
        }
    }

    // ------------------------------------
    // FILTER RECORDS BY DATE RANGE
    // ------------------------------------

    fun filterRecordsByDate(
        startDate: String,
        endDate: String
    ) {

        searchJob?.cancel()

        if (
            !isValidDate(startDate) ||
            !isValidDate(endDate) ||
            startDate > endDate
        ) {
            showError("Please select a valid date range.")
            return
        }

        searchJob = viewModelScope.launch {

            try {
                repository.filterRecordsByDate(
                    startDate,
                    endDate
                ).collectLatest { results ->

                    _uiState.value = _uiState.value.copy(
                        searchResults = results,
                        errorMessage = null
                    )
                }

            } catch (e: Exception) {

                showError(
                    e.message ?: "Unable to filter records."
                )
            }
        }
    }



    fun clearSearch() {

        searchJob?.cancel()
        searchJob = null

        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            errorMessage = null
        )
    }

    // ------------------------------------
    // GET RECORD BY ID
    // ------------------------------------

    suspend fun getRecordById(
        recordId: Int
    ): HealthRecord? {

        return try {
            repository.getRecordById(recordId)
        } catch (e: Exception) {
            showError(
                e.message ?: "Unable to retrieve record."
            )
            null
        }
    }

    // ------------------------------------
    // CLEAR MESSAGES
    // ------------------------------------

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    // ------------------------------------
    // HELPER FUNCTIONS
    // ------------------------------------

    private fun getTodayDate(): String {

        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }

    private fun isValidDate(date: String): Boolean {

        return try {

            val format = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.ROOT
            )

            format.isLenient = false

            val parsedDate = format.parse(date)

            parsedDate != null &&
                    format.format(parsedDate) == date

        } catch (e: Exception) {
            false
        }
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }
}
