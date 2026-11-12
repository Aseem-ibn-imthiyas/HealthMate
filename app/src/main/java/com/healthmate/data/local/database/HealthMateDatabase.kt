
package com.healthmate.data.local.database

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.healthmate.data.local.dao.HealthRecordDao
import com.healthmate.data.local.dao.UserDao

import com.healthmate.data.local.entity.HealthRecord
import com.healthmate.data.local.entity.User
import com.healthmate.data.local.dao.UserSessionDao
import com.healthmate.data.local.entity.UserSession


@Database(
    entities = [
        HealthRecord::class,
        User::class,
        UserSession::class
    ],
    version = 3,
    exportSchema = false
)




abstract class HealthMateDatabase : RoomDatabase() {

    abstract fun healthRecordDao(): HealthRecordDao

    abstract fun userSessionDao(): UserSessionDao

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: HealthMateDatabase? = null

        // Migration from database version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `users` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `fullName` TEXT NOT NULL,
                        `email` TEXT NOT NULL,
                        `passwordHash` TEXT NOT NULL,
                        `passwordSalt` TEXT NOT NULL,
                        `createdAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS 
                    `index_users_email` ON `users` (`email`)
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `user_sessions` (
                `sessionId` INTEGER NOT NULL,
                `userId` INTEGER NOT NULL,
                `createdAt` TEXT NOT NULL,
                PRIMARY KEY(`sessionId`)
            )
            """.trimIndent()
                )
            }
        }

        fun getDatabase(
            context: Context
        ): HealthMateDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthMateDatabase::class.java,
                    "healthmate_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
