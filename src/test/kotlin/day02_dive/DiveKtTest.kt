package day02_dive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    forward 5
    down 5
    forward 8
    up 3
    down 8
    forward 2
""".trimIndent()

internal class DiveKtTest {

    @Test
    fun partOne() {
        assertEquals(150, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(900, partTwo(WORKED_EXAMPLE))
    }

}
