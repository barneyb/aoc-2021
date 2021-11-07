package aoc2015.day02_wrap_presents

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Wrap_presentsKtTest {

    @Test
    fun partOne() {
        assertEquals(58, partOne("2x3x4"))
        assertEquals(43, partOne("1x1x10"))
    }

    @Test
    fun partTwo() {
        assertEquals(34, partTwo("2x3x4"))
        assertEquals(14, partTwo("1x1x10"))
    }

}
