package aoc2019.day03_crossed_wires

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CrossedWiresKtTest {

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

