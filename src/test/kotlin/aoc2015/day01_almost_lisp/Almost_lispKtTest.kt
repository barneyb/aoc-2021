package aoc2015.day01_almost_lisp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class Almost_lispKtTest {

    @Test
    fun partOne() {
        assertEquals(0, partOne("(())"))
        assertEquals(0, partOne("()()"))
        assertEquals(3, partOne("((("))
        assertEquals(3, partOne("(()(()("))
        assertEquals(3, partOne("))((((("))
        assertEquals(-1, partOne("())"))
        assertEquals(-1, partOne("))("))
        assertEquals(-3, partOne(")))"))
        assertEquals(-3, partOne(")())())"))
    }

    @Test
    fun partTwo() {
        assertThrows<IllegalArgumentException> {
            partTwo("")
        }
        assertEquals(1, partTwo(")"))
        assertThrows<IllegalArgumentException> {
            partTwo("(")
        }
        assertEquals(5, partTwo("()())"))
        assertThrows<IllegalArgumentException> {
            partTwo("()()(")
        }
    }

}
