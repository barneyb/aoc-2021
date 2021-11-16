package aoc2016.day02_bathroom_security

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    ULL
    RRDDD
    LURDL
    UUUUD
""".trimIndent()

internal class BathroomSecurityKtTest {

    @Test
    fun partOne() {
        assertEquals("1", partOne("UL"))
        assertEquals("1", partOne("UULL"))
        assertEquals("1985", partOne(WORKED_EXAMPLE))
    }

    @Test
    fun parseKeymap() {
        assertArrayEquals(
            arrayOf(
                intArrayOf(), // no button zero
                intArrayOf(1, 2, 4, 1),
                intArrayOf(2, 3, 5, 1),
                intArrayOf(3, 3, 6, 2),
                intArrayOf(1, 5, 7, 4),
                intArrayOf(2, 6, 8, 4),
                intArrayOf(3, 6, 9, 5),
                intArrayOf(4, 8, 7, 7),
                intArrayOf(5, 9, 8, 7),
                intArrayOf(6, 9, 9, 8),
            ), """
                1 2 3
                4 5 6
                7 8 9
            """.asKeymap()
        )
    }

    @Test
    fun partTwo() {
        assertEquals("5DB3", partTwo(WORKED_EXAMPLE))
    }

}
