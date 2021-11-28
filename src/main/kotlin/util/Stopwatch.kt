package util

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Stopwatch(private val startedAt: Long = System.nanoTime()) {

    val elapsed: Duration
        get() = (System.nanoTime() - startedAt)
            .toDuration(DurationUnit.NANOSECONDS)

}
