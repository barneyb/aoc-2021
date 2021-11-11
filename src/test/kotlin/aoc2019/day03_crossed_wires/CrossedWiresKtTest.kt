package aoc2019.day03_crossed_wires

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CrossedWiresKtTest {

    @Test
    fun partOne() {
        assertEquals(
            6, partOne(
                """
            R8,U5,L5,D3
            U7,R6,D4,L4
        """.trimIndent()
            )
        )
        assertEquals(
            159, partOne(
                """
            R75,D30,R83,U83,L12,D49,R71,U7,L72
            U62,R66,U55,R34,D71,R55,D58,R83
        """.trimIndent()
            )
        )
        assertEquals(
            135, partOne(
                """
            R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
            U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
        """.trimIndent()
            )
        )
    }

    @Test
    fun partTwo() {
        assertEquals(1, partTwo("a"))
        assertEquals(1, partTwo(" a "))
        assertEquals(3, partTwo(" a b "))
    }

}

