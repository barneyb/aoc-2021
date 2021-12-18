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
    fun partOneLoop() {
        assertEquals(7, partOneLoop(WORKED_EXAMPLE))
    }

    @Test
    fun partOneFold() {
        assertEquals(7, partOneFold(WORKED_EXAMPLE))
    }

    @Test
    fun partOneZip() {
        assertEquals(7, partOneZip(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(5, partTwo(WORKED_EXAMPLE))
    }

    @Test
    fun csv() {
        assertEquals(
            """
            pos,depth,smooth_depth
            0,199,202
            1,200,206
            2,208,206
            3,210,205
            4,200,215
            5,207,238
            6,240,256
            7,269,264
            8,260,261
            9,263,263
            
        """.trimIndent(), csv(WORKED_EXAMPLE)
        )
    }

}
