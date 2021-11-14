package aoc2018.day01_chronal_calibration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.cleanInput

internal class ChronalCalibrationKtTest {

    @Test
    fun partOne() {
        assertEquals(
            3, partOne(
                """
            +1
            -2
            +3
            +1
        """.cleanInput()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(-1, partTwo("input"))
    }

}

