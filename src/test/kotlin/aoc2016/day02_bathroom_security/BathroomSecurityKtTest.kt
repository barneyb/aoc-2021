package aoc2016.day02_bathroom_security

import geom2d.Point
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
    fun buttonToPoint() {
        assertEquals(Point(0, 0), 1.toButton())
        assertEquals(Point(1, 0), 2.toButton())
        assertEquals(Point(2, 0), 3.toButton())
        assertEquals(Point(0, 1), 4.toButton())
        assertEquals(Point(1, 1), 5.toButton())
        assertEquals(Point(2, 1), 6.toButton())
        assertEquals(Point(0, 2), 7.toButton())
        assertEquals(Point(1, 2), 8.toButton())
        assertEquals(Point(2, 2), 9.toButton())
    }

    @Test
    fun partOne() {
        assertEquals("1", partOne("UL"))
        assertEquals("1", partOne("UULL"))
        assertEquals("1985", partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo("input"))
    }

}
