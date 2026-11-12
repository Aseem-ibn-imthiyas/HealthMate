
package com.healthmate.data.repository

import com.healthmate.data.local.dao.HealthRecordDao
import com.healthmate.data.local.entity.HealthRecord

import kotlinx.coroutines.flow.Flow

class HealthRecordRepository(
    private val healthRecordDao: HealthRecordDao
) {

    // CREATE / UPDATE
    // One health record per date.
    suspend fun saveRecord(
        record: HealthRecord
    ): Long {

        val existingRecord =
            healthRecordDao.getRecordByDate(record.date)

        return if (existingRecord != null) {

            // Preserve the existing database ID.
            healthRecordDao.updateRecord(
                record.copy(id = existingRecord.id)
            )

            existingRecord.id.toLong()

        } else {

            val insertedId =
                healthRecordDao.insertRecord(record)

            if (insertedId != -1L) {
                insertedId
            } else {
                // Handle a date conflict safely.
                val conflictingRecord =
                    healthRecordDao.getRecordByDate(record.date)

                if (conflictingRecord != null) {
                    healthRecordDao.updateRecord(
                        record.copy(id = conflictingRecord.id)
                    )

                    conflictingRecord.id.toLong()
                } else {
                    throw IllegalStateException(
                        "Unable to save health record."
                    )
                }
            }
        }
    }

    // READ - Get all records
    fun getAllRecords(): Flow<List<HealthRecord>> {
        return healthRecordDao.getAllRecords()
    }

    // READ - Get record by ID
    suspend fun getRecordById(
        recordId: Int
    ): HealthRecord? {
        return healthRecordDao.getRecordById(recordId)
    }

    // READ - Get record by date
    suspend fun getRecordByDate(
        date: String
    ): HealthRecord? {
        return healthRecordDao.getRecordByDate(date)
    }

    // UPDATE
    suspend fun updateRecord(
        record: HealthRecord
    ) {
        healthRecordDao.updateRecord(record)
    }

    // DELETE
    suspend fun deleteRecord(
        record: HealthRecord
    ) {
        healthRecordDao.deleteRecord(record)
    }

    // DELETE - By ID
    suspend fun deleteRecordById(
        recordId: Int
    ) {
        healthRecordDao.deleteRecordById(recordId)
    }

    // SEARCH - By date
    fun searchRecords(
        searchDate: String
    ): Flow<List<HealthRecord>> {
        return healthRecordDao.searchRecords(searchDate)
    }

    // FILTER - Date range
    fun filterRecordsByDate(
        startDate: String,
        endDate: String
    ): Flow<List<HealthRecord>> {
        return healthRecordDao.searchRecordsByDate(
            startDate,
            endDate
        )
    }

    // DASHBOARD - Today's record
    fun getTodayRecord(
        today: String
    ): Flow<HealthRecord?> {
        return healthRecordDao.getTodayRecord(today)
    }

    // Total records
    fun getRecordCount(): Flow<Int> {
        return healthRecordDao.getRecordCount()
    }
}
