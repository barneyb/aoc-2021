package day23_amphipod

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    #############
    #...........#
    ###B#C#B#D###
      #A#D#C#A#  
      #########  
""".trimIndent()

internal class AmphipodKtTest {

    @Test
    fun partOne() {
        assertEquals(12521, partOne(WORKED_EXAMPLE))
    }

    @Test
    fun partTwo() {
        assertEquals(44169, partTwo(WORKED_EXAMPLE))
    }

}
