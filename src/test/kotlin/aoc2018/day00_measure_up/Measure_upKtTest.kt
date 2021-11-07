package aoc2018.day00_measure_up

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Measure_upKtTest {

    @Test
    fun partOne() {
        assertEquals(1, partOne("a"))
        assertEquals(3, partOne(" a "))
        assertEquals(3, partOne("cat"))
    }

    @Test
    fun partTwo() {
        assertEquals(1, partTwo("a"))
        assertEquals(1, partTwo(" a "))
        assertEquals(3, partTwo(" a b "))
    }

}
