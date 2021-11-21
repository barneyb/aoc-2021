package aoc2020.day02_password_philosophy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    1-3 a: abcde
    1-3 b: cdefg
    2-9 c: ccccccccc
""".trimIndent()

internal class PasswordPhilosophyKtTest {

    @Test
    fun partOne() {
        assertEquals(2, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(1, partTwo(WORKED_EXAMPLE))
    }

}
