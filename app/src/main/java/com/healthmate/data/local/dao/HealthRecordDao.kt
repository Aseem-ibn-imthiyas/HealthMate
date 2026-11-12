package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.healthmate.data.local.entity.HealthRecord

import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(
        record: HealthRecord
    ): Long


    // READ - Get all records
    @Query(
        "SELECT * FROM health_records ORDER BY date DESC"
    )
    fun getAllRecords(): Flow<List<HealthRecord>>


    // READ - Get record by ID
    @Query(
        "SELECT * FROM health_records WHERE id = :recordId"
    )
    suspend fun getRecordById(
        recordId: Int
    ): HealthRecord?


    // READ - Get record by date
    @Query(
        "SELECT * FROM health_records WHERE date = :date LIMIT 1"
    )
    suspend fun getRecordByDate(
        date: String
    ): HealthRecord?


    // UPDATE
    @Update
    suspend fun updateRecord(
        record: HealthRecord
    )


    // DELETE
    @Delete
    suspend fun deleteRecord(
        record: HealthRecord
    )


    // DELETE - By ID
    @Query(
        "DELETE FROM health_records WHERE id = :recordId"
    )
    suspend fun deleteRecordById(
        recordId: Int
    )


    // SEARCH - Records within a date range
    @Query(
        """
        SELECT * FROM health_records
        WHERE date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
        """
    )
    fun searchRecordsByDate(
        startDate: String,
        endDate: String
    ): Flow<List<HealthRecord>>


    // SEARCH - Specific date
    @Query(
        """
        SELECT * FROM health_records
        WHERE date LIKE '%' || :searchDate || '%'
        ORDER BY date DESC
        """
    )
    fun searchRecords(
        searchDate: String
    ): Flow<List<HealthRecord>>


    // READ - Today's record
    @Query(
        "SELECT * FROM health_records WHERE date = :today LIMIT 1"
    )
    fun getTodayRecord(
        today: String
    ): Flow<HealthRecord?>


    // READ - Total number of records
    @Query(
        "SELECT COUNT(*) FROM health_records"
    )
    fun getRecordCount(): Flow<Int>
}
