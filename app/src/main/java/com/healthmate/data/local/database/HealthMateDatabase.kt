
package com.healthmate.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.healthmate.data.local.dao.HealthRecordDao
import com.healthmate.data.local.dao.UserDao
import com.healthmate.data.local.dao.UserSessionDao
import com.healthmate.data.local.entity.HealthRecord
import com.healthmate.data.local.entity.User
import com.healthmate.data.local.entity.UserSession
import com.healthmate.data.local.entity.Community
import com.healthmate.data.local.entity.CommunityMember
import com.healthmate.data.local.entity.CommunityPost
import com.healthmate.data.local.entity.CommunityLike
import com.healthmate.data.local.entity.CommunityComment
import com.healthmate.data.local.dao.CommunityDao
import com.healthmate.data.local.dao.CommunityMemberDao
import com.healthmate.data.local.dao.CommunityPostDao
import com.healthmate.data.local.dao.CommunityLikeDao
import com.healthmate.data.local.dao.CommunityCommentDao
import com.healthmate.data.local.entity.UserSettings
import com.healthmate.data.local.dao.UserSettingsDao
import com.healthmate.data.local.dao.NotificationDao
import com.healthmate.data.local.entity.NotificationEntity

@Database(
    entities = [
        HealthRecord::class,
        User::class,
        UserSession::class,
        Community::class,
        CommunityMember::class,
        CommunityPost::class,
        CommunityLike::class,
        CommunityComment::class,
        UserSettings::class,
        NotificationEntity::class
    ],
    version = 11,
    exportSchema = false
)

    abstract class HealthMateDatabase : RoomDatabase() {

    abstract fun healthRecordDao(): HealthRecordDao

    abstract fun userSessionDao(): UserSessionDao

    abstract fun userDao(): UserDao

    abstract fun communityDao(): CommunityDao

    abstract fun communityMemberDao(): CommunityMemberDao

    abstract fun communityPostDao(): CommunityPostDao

    abstract fun communityLikeDao(): CommunityLikeDao

    abstract fun communityCommentDao(): CommunityCommentDao

    abstract fun userSettingsDao(): UserSettingsDao

    abstract fun notificationDao(): NotificationDao

    companion object {

        @Volatile
        private var INSTANCE: HealthMateDatabase? = null

        // Migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {

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

        // Migration from version 2 to version 3
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

        // Migration from version 3 to version 4
        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(database: SupportSQLiteDatabase) {

                // Communities table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `communities` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `createdBy` INTEGER NOT NULL,
                        `createdAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_communities_createdBy`
                    ON `communities` (`createdBy`)
                    """.trimIndent()
                )

                // Community members table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `community_members` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `communityId` INTEGER NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `joinedAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS
                    `index_community_members_communityId_userId`
                    ON `community_members` (`communityId`, `userId`)
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_members_userId`
                    ON `community_members` (`userId`)
                    """.trimIndent()
                )

                // Community posts table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `community_posts` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `communityId` INTEGER NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `content` TEXT NOT NULL,
                        `createdAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_posts_communityId`
                    ON `community_posts` (`communityId`)
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_posts_userId`
                    ON `community_posts` (`userId`)
                    """.trimIndent()
                )

                // Community likes table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `community_likes` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `postId` INTEGER NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `createdAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS
                    `index_community_likes_postId_userId`
                    ON `community_likes` (`postId`, `userId`)
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_likes_userId`
                    ON `community_likes` (`userId`)
                    """.trimIndent()
                )

                // Community comments table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `community_comments` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `postId` INTEGER NOT NULL,
                        `userId` INTEGER NOT NULL,
                        `content` TEXT NOT NULL,
                        `createdAt` TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_comments_postId`
                    ON `community_comments` (`postId`)
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    `index_community_comments_userId`
                    ON `community_comments` (`userId`)
                    """.trimIndent()
                )
            }
        }


        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
            ALTER TABLE communities
            ADD COLUMN coverImageUri TEXT
            """.trimIndent()
                )

                database.execSQL(
                    """
            ALTER TABLE communities
            ADD COLUMN privacy TEXT NOT NULL DEFAULT 'Public'
            """.trimIndent()
                )

                database.execSQL(
                    """
            ALTER TABLE communities
            ADD COLUMN guidelines TEXT NOT NULL DEFAULT ''
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE users ADD COLUMN profileImageUri TEXT"
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE community_posts ADD COLUMN imageUri TEXT"
                )
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `user_settings` (
                `userId` INTEGER NOT NULL,
                `notificationsEnabled` INTEGER NOT NULL,
                PRIMARY KEY(`userId`)
            )
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            ALTER TABLE user_settings
            ADD COLUMN darkModeEnabled INTEGER NOT NULL DEFAULT 0
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_9_10 = object : Migration(9, 10) {

            override fun migrate(database: SupportSQLiteDatabase) {

                // Create the new health_records table
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `health_records_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `date` TEXT NOT NULL,
                `steps` INTEGER NOT NULL,
                `calories` INTEGER NOT NULL,
                `water` INTEGER NOT NULL,
                `userId` INTEGER NOT NULL
            )
            """.trimIndent()
                )

                // Preserve existing records and assign the active user's ID
                database.execSQL(
                    """
            INSERT INTO `health_records_new`
                (`id`, `date`, `steps`, `calories`, `water`, `userId`)
            SELECT
                `id`,
                `date`,
                `steps`,
                `calories`,
                `water`,
                COALESCE(
                    (SELECT `userId`
                     FROM `user_sessions`
                     WHERE `sessionId` = 1),
                    0
                )
            FROM `health_records`
            """.trimIndent()
                )

                // Replace the old table
                database.execSQL("DROP TABLE `health_records`")

                database.execSQL(
                    "ALTER TABLE `health_records_new` RENAME TO `health_records`"
                )

                // Create the composite unique index
                database.execSQL(
                    """
            CREATE UNIQUE INDEX `index_health_records_userId_date`
            ON `health_records` (`userId`, `date`)
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `notifications` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `userId` INTEGER NOT NULL,
                `title` TEXT NOT NULL,
                `message` TEXT NOT NULL,
                `type` TEXT NOT NULL,
                `createdAt` INTEGER NOT NULL,
                `isRead` INTEGER NOT NULL
            )
        """.trimIndent())

                database.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_notifications_userId`
            ON `notifications` (`userId`)
        """.trimIndent())

                database.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_notifications_userId_createdAt`
            ON `notifications` (`userId`, `createdAt`)
        """.trimIndent())
            }
        }

        fun getDatabase(context: Context): HealthMateDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthMateDatabase::class.java,
                    "healthmate_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11
                    )
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
