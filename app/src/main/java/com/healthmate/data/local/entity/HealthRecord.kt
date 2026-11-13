
package com.healthmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "health_records",
    indices = [
        Index(
            value = ["userId", "date"],
            unique = true
        )
    ]
)
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,

    val steps: Int,

    val calories: Int,

    val water: Int,

    val userId: Int
)
