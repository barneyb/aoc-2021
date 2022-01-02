package geom

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VecTest {

    @Test
    fun manDist() {
        assertEquals(3, Vec(1, 2).manhattanDistance())
        assertEquals(4, Vec(1, 2).manhattanDistance(Vec(3, 4)))
    }

}
