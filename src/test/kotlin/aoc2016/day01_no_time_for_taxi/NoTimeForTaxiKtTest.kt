package aoc2016.day01_no_time_for_taxi

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NoTimeForTaxiKtTest {

    @Test
    fun partOne() {
        assertEquals(5, partOne("R2, L3"))
        assertEquals(2, partOne("R2, R2, R2"))
        assertEquals(12, partOne("R5, L5, R5, R3"))
    }

    @Test
    fun partTwo() {
        assertEquals(4, partTwo("R8, R4, R4, R8"))
    }

}
