package aoc2017.day02_corruption_checksum

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CorruptionChecksumKtTest {

    @Test
    fun partOne() {
        assertEquals(
            18, partOne(
                """
            5 1 9 5
            7 5 3
            2 4 6 8
        """.trimIndent()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo("input"))
    }

}
