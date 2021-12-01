package day01_sonar_sweep

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    199
    200
    208
    210
    200
    207
    240
    269
    260
    263
""".trimIndent()

internal class SonarSweepKtTest {

    @Test
    fun partOne() {
        assertEquals(7, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
