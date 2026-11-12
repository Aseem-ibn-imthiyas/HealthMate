
package com.healthmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.healthmate.data.local.entity.User

@Dao
interface UserDao {

    // REGISTER - Insert new user
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    // LOGIN - Find user by email
    @Query(
        "SELECT * FROM users WHERE email = :email LIMIT 1"
    )
    suspend fun getUserByEmail(
        email: String
    ): User?

    // GET - Find user by ID
    @Query(
        "SELECT * FROM users WHERE id = :userId LIMIT 1"
    )
    suspend fun getUserById(
        userId: Int
    ): User?

    // UPDATE - Change user details or password
    @Update
    suspend fun updateUser(
        user: User
    )

    // CHECK - Verify whether email already exists
    @Query(
        "SELECT COUNT(*) > 0 FROM users WHERE email = :email"
    )
    suspend fun isEmailRegistered(
        email: String
    ): Boolean

    // DELETE - Delete user account
    @Query(
        "DELETE FROM users WHERE id = :userId"
    )
    suspend fun deleteUser(
        userId: Int
    )
}
