package day06_lanternfish_growth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    3,4,3,1,2
""".trimIndent()

internal class LanternfishGrowthKtTest {

    @Test
    fun partOneDayZero() {
        assertEquals(5, partOne(WORKED_EXAMPLE, 0))
    }

    @Test
    fun partOneDayOne() {
        assertEquals(5, partOne(WORKED_EXAMPLE, 1))
    }

    @Test
    fun partOneDayTwo() {
        assertEquals(6, partOne(WORKED_EXAMPLE, 2))
    }

    @Test
    fun partOneDay18() {
        assertEquals(26, partOne(WORKED_EXAMPLE, 18))
    }

    @Test
    fun partOne() {
        assertEquals(5934, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
