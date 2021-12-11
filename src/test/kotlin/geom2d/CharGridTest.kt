package geom2d

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

internal class CharGridTest {

    private fun buildGrid() =
        CharGrid(
            """
                123
                456
                789
            """.trimIndent(),
            Char::digitToInt
        )

    @Test
    fun construct() {
        val g = buildGrid()
        assertEquals(1, g[Point(0, 0)])
        assertEquals(3, g[Point(2, 0)])
        assertEquals(5, g[Point(1, 1)])
        assertEquals(7, g[Point(0, 2)])
        assertEquals(9, g[Point(2, 2)])
    }

    @Test
    fun allPoints() {
        val itr = buildGrid()
            .allPoints()
            .iterator()
        assertEquals(Point(0, 0), itr.next())
        assertEquals(Point(1, 0), itr.next())
        assertEquals(Point(2, 0), itr.next())
        assertEquals(Point(0, 1), itr.next())
        assertEquals(Point(1, 1), itr.next())
        assertEquals(Point(2, 1), itr.next())
        assertEquals(Point(0, 2), itr.next())
        assertEquals(Point(1, 2), itr.next())
        assertEquals(Point(2, 2), itr.next())
        assertFalse(itr.hasNext())
        assertFalse(itr.hasNext())
    }

    @Test
    fun orthogonalNeighbors_middle() {
        val itr = buildGrid()
            .neighbors(Point(1, 1))
            .iterator()
        assertEquals(Point(1, 0), itr.next())
        assertEquals(Point(2, 1), itr.next())
        assertEquals(Point(1, 2), itr.next())
        assertEquals(Point(0, 1), itr.next())
        assertFalse(itr.hasNext())
        assertFalse(itr.hasNext())
    }

    @Test
    fun orthogonalNeighbors_top() {
        val itr = buildGrid()
            .neighbors(Point(1, 0))
            .iterator()
        assertEquals(Point(2, 0), itr.next())
        assertEquals(Point(1, 1), itr.next())
        assertEquals(Point(0, 0), itr.next())
        assertFalse(itr.hasNext())
        assertFalse(itr.hasNext())
    }

    @Test
    fun orthogonalNeighbors_right() {
        val itr = buildGrid()
            .neighbors(Point(0, 1))
            .iterator()
        assertEquals(Point(0, 0), itr.next())
        assertEquals(Point(1, 1), itr.next())
        assertEquals(Point(0, 2), itr.next())
        assertFalse(itr.hasNext())
        assertFalse(itr.hasNext())
    }

    @Test
    fun orthogonalNeighbors_bottom_left() {
        val itr = buildGrid()
            .neighbors(Point(0, 2))
            .iterator()
        assertEquals(Point(0, 1), itr.next())
        assertEquals(Point(1, 2), itr.next())
        assertFalse(itr.hasNext())
        assertFalse(itr.hasNext())
    }

}
