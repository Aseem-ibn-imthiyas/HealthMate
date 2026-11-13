
package com.healthmate.data.repository

import com.healthmate.data.local.dao.UserDao
import com.healthmate.data.local.entity.User

class UserRepository(
    private val userDao: UserDao
) {

    // REGISTER - Create a new account
    suspend fun registerUser(
        user: User
    ): Long {

        val normalizedEmail = user.email.trim().lowercase()

        if (userDao.isEmailRegistered(normalizedEmail)) {
            return -1L
        }

        return userDao.insertUser(
            user.copy(email = normalizedEmail)
        )
    }

    // LOGIN - Find account using email
    suspend fun getUserByEmail(
        email: String
    ): User? {

        return userDao.getUserByEmail(
            email.trim().lowercase()
        )
    }

    // GET - Retrieve account by ID
    suspend fun getUserById(
        userId: Int
    ): User? {

        return userDao.getUserById(userId)
    }

    // CHECK - Verify registered email
    suspend fun isEmailRegistered(
        email: String
    ): Boolean {

        return userDao.isEmailRegistered(
            email.trim().lowercase()
        )
    }

    // UPDATE - Change account information or password
    suspend fun updateUser(
        user: User
    ) {

        userDao.updateUser(
            user.copy(email = user.email.trim().lowercase())
        )
    }

    // UPDATE - Save profile information
    suspend fun updateProfile(
        userId: Int,
        fullName: String,
        email: String,
        profileImageUri: String?
    ): User? {

        val normalizedEmail = email.trim().lowercase()

        // Check whether another account uses this email
        val existingUser = userDao.getUserByEmail(normalizedEmail)

        if (existingUser != null && existingUser.id != userId) {
            return null
        }

        // Retrieve the existing account
        val currentUser = userDao.getUserById(userId)
            ?: return null

        // Preserve password and other account information
        val updatedUser = currentUser.copy(
            fullName = fullName.trim(),
            email = normalizedEmail,
            profileImageUri = profileImageUri
        )

        userDao.updateUser(updatedUser)

        return updatedUser
    }

    // DELETE - Remove user account
    suspend fun deleteUser(
        userId: Int
    ) {

        userDao.deleteUser(userId)
    }
}
