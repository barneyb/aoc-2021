package day11_dumbo_octopus

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val LOW_START = """
    11111
    19991
    19191
    19991
    11111
""".trimIndent()
private val WORKED_EXAMPLE = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
""".trimIndent()

internal class DumboOctopusKtTest {

    @Test
    fun lowStartingEnergy() {
        assertEquals(9, partOne(LOW_START, 1))
        assertEquals(9, partOne(LOW_START, 2)) // nothing the second step
    }

    @Test
    fun partOne() {
        assertEquals(0, partOne(WORKED_EXAMPLE, 1))
        assertEquals(35, partOne(WORKED_EXAMPLE, 2))
        assertEquals(204, partOne(WORKED_EXAMPLE, 10))
        assertEquals(1656, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
