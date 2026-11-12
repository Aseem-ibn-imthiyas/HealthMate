
package com.healthmate.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {

    private const val ITERATIONS = 310000
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16

    private const val ALGORITHM = "PBKDF2WithHmacSHA1"

    // Generate a random salt for each password
    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return salt
    }

    // Generate password hash
    private fun generateHash(
        password: CharArray,
        salt: ByteArray
    ): ByteArray {

        val spec = PBEKeySpec(
            password,
            salt,
            ITERATIONS,
            KEY_LENGTH
        )

        return try {
            SecretKeyFactory
                .getInstance(ALGORITHM)
                .generateSecret(spec)
                .encoded
        } finally {
            spec.clearPassword()
        }
    }

    // Create password hash and salt
    fun hashPassword(password: String): Pair<String, String> {

        val salt = generateSalt()

        val hash = generateHash(
            password.toCharArray(),
            salt
        )

        return Pair(
            Base64.encodeToString(hash, Base64.NO_WRAP),
            Base64.encodeToString(salt, Base64.NO_WRAP)
        )
    }

    // Verify entered password
    fun verifyPassword(
        password: String,
        storedHash: String,
        storedSalt: String
    ): Boolean {

        return try {

            val salt = Base64.decode(
                storedSalt,
                Base64.NO_WRAP
            )

            val expectedHash = Base64.decode(
                storedHash,
                Base64.NO_WRAP
            )

            val actualHash = generateHash(
                password.toCharArray(),
                salt
            )

            java.security.MessageDigest.isEqual(
                expectedHash,
                actualHash
            )

        } catch (e: Exception) {
            false
        }
    }
}
