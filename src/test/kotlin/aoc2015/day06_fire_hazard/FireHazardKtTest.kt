package aoc2015.day06_fire_hazard

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    input
""".trimIndent()

internal class FireHazardKtTest {

    @Test
    fun partOne() {
        assertEquals(1_000_000, partOne("turn on 0,0 through 999,999"))
        assertEquals(1_000, partOne("toggle 0,0 through 999,0"))
        assertEquals(0, partOne("turn off 499,499 through 500,500"))
        assertEquals(
            1_000_000 - 1_000 - 4, partOne(
                """
                turn on 0,0 through 999,999
                toggle 0,0 through 999,0
                turn off 499,499 through 500,500
                """.trimIndent()
            )
        )
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
