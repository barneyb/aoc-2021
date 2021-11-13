package aoc2015.day03_spherical_houses

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SphericalHousesKtTest {

    @Test
    fun partOne() {
        assertEquals(2, partOne(">"))
        assertEquals(4, partOne("^>v<"))
        assertEquals(2, partOne("^v^v^v^v^v"))
    }

    @Test
    fun partTwo() {
        assertEquals(3, partTwo("^v"))
        assertEquals(3, partTwo("^>v<"))
        assertEquals(11, partTwo("^v^v^v^v^v"))
    }

}

