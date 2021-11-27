package aoc2020.day05_binary_boarding

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    input
""".trimIndent()

internal class BinaryBoardingKtTest {

    @Test
    fun seatId() {
        assertEquals(357, seatId("FBFBBFFRLR"))
        assertEquals(567, seatId("BFFFBBFRRR"))
        assertEquals(119, seatId("FFFBBBFRRR"))
        assertEquals(820, seatId("BBFFBBFRLL"))
    }

    @Test
    fun partOne() {
        assertEquals(
            820, partOne(
                """
            FBFBBFFRLR
            BFFFBBFRRR
            FFFBBBFRRR
            BBFFBBFRLL
        """.trimIndent()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
