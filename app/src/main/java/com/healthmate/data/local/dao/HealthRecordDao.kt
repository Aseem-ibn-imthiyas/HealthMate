
package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.HealthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecord(record: HealthRecord): Long

    // READ - Get all records belonging to a user
    @Query(
        """
        SELECT * FROM health_records
        WHERE userId = :userId
        ORDER BY date DESC
        """
    )
    fun getAllRecords(userId: Int): Flow<List<HealthRecord>>

    // READ - Get record by ID and owner
    @Query(
        """
        SELECT * FROM health_records
        WHERE id = :recordId AND userId = :userId
        LIMIT 1
        """
    )
    suspend fun getRecordById(
        recordId: Int,
        userId: Int
    ): HealthRecord?

    // READ - Get record by date and owner
    @Query(
        """
        SELECT * FROM health_records
        WHERE date = :date AND userId = :userId
        LIMIT 1
        """
    )
    suspend fun getRecordByDate(
        date: String,
        userId: Int
    ): HealthRecord?

    // UPDATE - Only update the owner's record
    @Query(
        """
        UPDATE health_records
        SET date = :date,
            steps = :steps,
            calories = :calories,
            water = :water
        WHERE id = :recordId AND userId = :userId
        """
    )
    suspend fun updateRecord(
        recordId: Int,
        date: String,
        steps: Int,
        calories: Int,
        water: Int,
        userId: Int
    ): Int

    // DELETE - Only delete the owner's record
    @Query(
        """
        DELETE FROM health_records
        WHERE id = :recordId AND userId = :userId
        """
    )
    suspend fun deleteRecordById(
        recordId: Int,
        userId: Int
    ): Int

    // SEARCH - Specific date
    @Query(
        """
        SELECT * FROM health_records
        WHERE userId = :userId
        AND date LIKE '%' || :searchDate || '%'
        ORDER BY date DESC
        """
    )
    fun searchRecords(
        searchDate: String,
        userId: Int
    ): Flow<List<HealthRecord>>

    // FILTER - Date range
    @Query(
        """
        SELECT * FROM health_records
        WHERE userId = :userId
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
        """
    )
    fun searchRecordsByDate(
        startDate: String,
        endDate: String,
        userId: Int
    ): Flow<List<HealthRecord>>

    // DASHBOARD - Today's record
    @Query(
        """
        SELECT * FROM health_records
        WHERE date = :today AND userId = :userId
        LIMIT 1
        """
    )
    fun getTodayRecord(
        today: String,
        userId: Int
    ): Flow<HealthRecord?>

    // TOTAL - User-specific record count
    @Query(
        """
        SELECT COUNT(*) FROM health_records
        WHERE userId = :userId
        """
    )
    fun getRecordCount(userId: Int): Flow<Int>
}
