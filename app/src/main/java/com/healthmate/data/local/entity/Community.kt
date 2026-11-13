package com.healthmate.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "communities",
    indices = [Index(value = ["createdBy"])]
)
data class Community(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val category: String,
    val createdBy: Int,
    val createdAt: String,

    val coverImageUri: String? = null,

    @ColumnInfo(defaultValue = "'Public'")
    val privacy: String = "Public",

    @ColumnInfo(defaultValue = "''")
    val guidelines: String = ""
)