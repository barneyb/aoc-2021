package day23_amphipod

import geom2d.Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    #############
    #...........#
    ###B#C#B#D###
      #A#D#C#A#  
      #########  
""".trimIndent()

private val EXPANDED_TWO = """
    #############
    #...........#
    ###B#C#B#D###
      #D#C#B#A#  
      #D#B#A#C#  
      #A#D#C#A#  
      #########  
""".trimIndent()

internal class GridTest {

    @Test
    fun parseAndStringify() {
        assertEquals(WORKED_EXAMPLE, WORKED_EXAMPLE.toGrid().toString())
        assertEquals(EXPANDED_TWO, EXPANDED_TWO.toGrid().toString())
    }

    @Test
    fun get() {
        val grid = EXPANDED_TWO.toGrid()
        assertEquals(Cell.WALL, grid[Point(5, 0)])
        assertEquals(Cell.OPEN, grid[Point(5, 1)])
        assertEquals(Cell.COPPER, grid[Point(5, 2)])
        assertEquals(Cell.COPPER, grid[Point(5, 3)])
        assertEquals(Cell.BRONZE, grid[Point(5, 4)])
        assertEquals(Cell.DESERT, grid[Point(5, 5)])
        assertEquals(Cell.WALL, grid[Point(5, 6)])
    }

    @Test
    fun completeness() {
        assertFalse(WORKED_EXAMPLE.toGrid().complete)
        assertFalse(EXPANDED_TWO.toGrid().complete)
        assertTrue(
            """
            #############
            #...........#
            ###A#B#C#D###
              #A#B#C#D#
              #########
            """.trimIndent().toGrid().complete
        )
        assertTrue(
            """
            #############
            #...........#
            ###A#B#C#D###
              #A#B#C#D#
              #A#B#C#D#
              #A#B#C#D#
              #########
            """.trimIndent().toGrid().complete
        )
        assertFalse(
            """
            #############
            #...........#
            ###A#B#C#D###
              #A#B#C#D#
              #A#B#C#D#
              #A#B#C#A#
              #########
            """.trimIndent().toGrid().complete
        )
        assertFalse(
            """
            #############
            #...........#
            ###A#B#C#D###
              #A#B#C#D#
              #A#B#C#D#
              #B#B#C#D#
              #########
            """.trimIndent().toGrid().complete
        )
        assertFalse(
            """
            #############
            #...........#
            ###B#B#C#D###
              #A#B#C#D#
              #A#B#C#D#
              #A#B#C#D#
              #########
            """.trimIndent().toGrid().complete
        )
        assertFalse(
            """
            #############
            #...........#
            ###A#B#C#A###
              #A#B#C#D#
              #A#B#C#D#
              #A#B#C#D#
              #########
            """.trimIndent().toGrid().complete
        )
    }

}
