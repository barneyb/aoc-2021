package day19_beacon_scanner

import geom.Vec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ScannerTest {

    @Test
    fun rotations() {
        val s = Scanner(0)
        fun tick(): Vec {
            s.twist()
            return s.beacons.first()
        }
        s.add(Vec(1, 2, 3))
        assertEquals(Vec(1, 2, 3), s.beacons.first())
        assertEquals(Vec(1, 3, -2), tick())
        assertEquals(Vec(1, -2, -3), tick())
        assertEquals(Vec(1, -3, 2), tick())

        assertEquals(Vec(-1, 2, 3), tick())
        assertEquals(Vec(-1, 3, -2), tick())
        assertEquals(Vec(-1, -2, -3), tick())
        assertEquals(Vec(-1, -3, 2), tick())

        assertEquals(Vec(2, 3, 1), tick())
        assertEquals(Vec(2, 1, -3), tick())
        assertEquals(Vec(2, -3, -1), tick())
        assertEquals(Vec(2, -1, 3), tick())

        assertEquals(Vec(-2, 3, 1), tick())
        assertEquals(Vec(-2, 1, -3), tick())
        assertEquals(Vec(-2, -3, -1), tick())
        assertEquals(Vec(-2, -1, 3), tick())

        assertEquals(Vec(3, 1, 2), tick())
        assertEquals(Vec(3, 2, -1), tick())
        assertEquals(Vec(3, -1, -2), tick())
        assertEquals(Vec(3, -2, 1), tick())

        assertEquals(Vec(-3, 1, 2), tick())
        assertEquals(Vec(-3, 2, -1), tick())
        assertEquals(Vec(-3, -1, -2), tick())
        assertEquals(Vec(-3, -2, 1), tick())

        assertEquals(Vec(1, 3, 2), tick())
        repeat(22) { tick() } // same as above...
        assertEquals(Vec(-2, -3, 1), tick())

        // and back to the start
        assertEquals(Vec(1, 2, 3), tick())
    }

}
