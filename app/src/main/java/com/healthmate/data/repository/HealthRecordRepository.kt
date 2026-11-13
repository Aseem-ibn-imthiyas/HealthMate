
package com.healthmate.data.repository

import com.healthmate.data.local.dao.HealthRecordDao
import com.healthmate.data.local.entity.HealthRecord
import kotlinx.coroutines.flow.Flow

class HealthRecordRepository(
    private val healthRecordDao: HealthRecordDao
) {

    // CREATE - Save record for a specific user
    suspend fun saveRecord(
        record: HealthRecord,
        userId: Int
    ): Long {

        val userRecord = record.copy(userId = userId)

        val existingRecord =
            healthRecordDao.getRecordByDate(
                userRecord.date,
                userId
            )

        return if (existingRecord != null) {

            healthRecordDao.updateRecord(
                recordId = existingRecord.id,
                date = userRecord.date,
                steps = userRecord.steps,
                calories = userRecord.calories,
                water = userRecord.water,
                userId = userId
            )

            existingRecord.id.toLong()

        } else {

            val insertedId =
                healthRecordDao.insertRecord(userRecord)

            if (insertedId != -1L) {
                insertedId
            } else {

                val conflictingRecord =
                    healthRecordDao.getRecordByDate(
                        userRecord.date,
                        userId
                    )

                if (conflictingRecord != null) {

                    healthRecordDao.updateRecord(
                        recordId = conflictingRecord.id,
                        date = userRecord.date,
                        steps = userRecord.steps,
                        calories = userRecord.calories,
                        water = userRecord.water,
                        userId = userId
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

    // READ - Get all records belonging to a user
    fun getAllRecords(userId: Int): Flow<List<HealthRecord>> {
        return healthRecordDao.getAllRecords(userId)
    }

    // READ - Get record by ID
    suspend fun getRecordById(
        recordId: Int,
        userId: Int
    ): HealthRecord? {
        return healthRecordDao.getRecordById(recordId, userId)
    }

    // READ - Get record by date
    suspend fun getRecordByDate(
        date: String,
        userId: Int
    ): HealthRecord? {
        return healthRecordDao.getRecordByDate(date, userId)
    }

    // UPDATE - Only update the user's own record
    suspend fun updateRecord(
        record: HealthRecord,
        userId: Int
    ) {
        val updatedRows = healthRecordDao.updateRecord(
            recordId = record.id,
            date = record.date,
            steps = record.steps,
            calories = record.calories,
            water = record.water,
            userId = userId
        )

        if (updatedRows == 0) {
            throw IllegalStateException(
                "Record not found or you do not have permission to update it."
            )
        }
    }

    // DELETE - Only delete the user's own record
    suspend fun deleteRecord(
        record: HealthRecord,
        userId: Int
    ) {
        deleteRecordById(record.id, userId)
    }

    // DELETE - By ID
    suspend fun deleteRecordById(
        recordId: Int,
        userId: Int
    ) {
        val deletedRows =
            healthRecordDao.deleteRecordById(recordId, userId)

        if (deletedRows == 0) {
            throw IllegalStateException(
                "Record not found or you do not have permission to delete it."
            )
        }
    }

    // SEARCH - By date
    fun searchRecords(
        searchDate: String,
        userId: Int
    ): Flow<List<HealthRecord>> {
        return healthRecordDao.searchRecords(searchDate, userId)
    }

    // FILTER - Date range
    fun filterRecordsByDate(
        startDate: String,
        endDate: String,
        userId: Int
    ): Flow<List<HealthRecord>> {
        return healthRecordDao.searchRecordsByDate(
            startDate,
            endDate,
            userId
        )
    }

    // DASHBOARD - Today's record
    fun getTodayRecord(
        today: String,
        userId: Int
    ): Flow<HealthRecord?> {
        return healthRecordDao.getTodayRecord(today, userId)
    }

    // TOTAL - User-specific record count
    fun getRecordCount(userId: Int): Flow<Int> {
        return healthRecordDao.getRecordCount(userId)
    }
}
