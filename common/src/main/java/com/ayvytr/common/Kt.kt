package com.ayvytr.common

import androidx.annotation.IntRange
import com.ayvytr.logger.L
import kotlinx.coroutines.delay

object Kt {
    suspend fun countDown(
        times: Int = c.COUNT_DOWN_60,
        @IntRange(from = 1)
        intervalMills: Int = 1000,
        action: (Int) -> Unit
    ) {
        repeat(times) {
            action(times - it - 1)
            L.e(times - it - 1)
            delay(intervalMills.toLong())
        }
    }
}
