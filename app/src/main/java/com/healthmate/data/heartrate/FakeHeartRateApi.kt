
package com.healthmate.data.heartrate

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

data class HeartRateReading(
    val bpm: Int,
    val timestamp: Long
)

object FakeHeartRateApi {

    fun observeHeartRate(): Flow<HeartRateReading> = flow {

        // Initial simulated reading
        var currentBpm = 75

        while (true) {

            // Generate a small realistic-looking variation
            currentBpm = (currentBpm + Random.nextInt(-3, 4))
                .coerceIn(65, 100)

            emit(
                HeartRateReading(
                    bpm = currentBpm,
                    timestamp = System.currentTimeMillis()
                )
            )

            // Update every 3 seconds
            delay(3000)
        }
    }
}
