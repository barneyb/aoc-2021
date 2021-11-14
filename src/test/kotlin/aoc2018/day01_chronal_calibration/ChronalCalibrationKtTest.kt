package aoc2018.day01_chronal_calibration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_INPUT = """
    +1
    -2
    +3
    +1
""".trimIndent()

internal class ChronalCalibrationKtTest {

    @Test
    fun partOne() {
        assertEquals(3, partOne(WORKED_INPUT))
    }

    @Test
    fun partTwo() {
        assertEquals(2, partTwo(WORKED_INPUT))
        assertEquals(0, partTwo("+1\n-1"))
        assertEquals(10, partTwo("+3\n+3\n+4\n-2\n-4"))
        assertEquals(5, partTwo("-6\n+3\n+8\n+5\n-6"))
        assertEquals(14, partTwo("+7\n+7\n-2\n-7\n-4"))

    }

}

