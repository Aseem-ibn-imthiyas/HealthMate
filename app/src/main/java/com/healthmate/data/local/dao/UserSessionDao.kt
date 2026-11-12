
package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmate.data.local.entity.UserSession

@Dao
interface UserSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: UserSession)

    @Query("SELECT * FROM user_sessions WHERE sessionId = 1 LIMIT 1")
    suspend fun getActiveSession(): UserSession?

    @Query("DELETE FROM user_sessions")
    suspend fun clearSession()
}
