package geom

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CuboidTest {

    @Test
    fun size() {
        Assertions.assertEquals(1, Cuboid(0L..0).size)
        Assertions.assertEquals(1, Cuboid(0L..0, 0L..0, 0L..0).size)
        Assertions.assertEquals(27, Cuboid(0L..2, 0L..2, 0L..2).size)
        Assertions.assertEquals(0, Cuboid(0L..2, 0L..2, EMPTY_RANGE).size)
    }

    @Test
    fun intersect() {
        val a = Cuboid(0L..2, 0L..2, 0L..2)
        // @formatter:off
        // identical
        Assertions.assertTrue(a.intersects(
            Cuboid(0L..2, 0L..2, 0L..2)))
        // inside
        Assertions.assertTrue(a.intersects(
            Cuboid(1L..1, 1L..1, 1L..1)))
        // corner
        Assertions.assertTrue(a.intersects(
            Cuboid(2L..3, 2L..3, 2L..3)))
        // edge
        Assertions.assertTrue(a.intersects(
            Cuboid(2L..3, 1L..1, 2L..3)))
        // face
        Assertions.assertTrue(a.intersects(
            Cuboid(2L..3, 1L..1, 1L..1)))
        // @formatter:on
    }

    @Test
    fun contains() {
        val a = Cuboid(0L..2, 0L..2, 0L..2)
        // @formatter:off
        // identical
        Assertions.assertTrue(a.contains(
            Cuboid(0L..2, 0L..2, 0L..2)))
        // inside
        Assertions.assertTrue(a.contains(
            Cuboid(1L..1, 1L..1, 1L..1)))
        // corner
        Assertions.assertFalse(a.contains(
            Cuboid(2L..3, 2L..3, 2L..3)))
        // edge
        Assertions.assertFalse(a.contains(
            Cuboid(2L..3, 1L..1, 2L..3)))
        // face
        Assertions.assertFalse(a.contains(
            Cuboid(2L..3, 1L..1, 1L..1)))
        // @formatter:on
    }

    @Test
    fun doesntIntersect() {
        val a = Cuboid(0L..2, 0L..2, 0L..2)
        // @formatter:off
        Assertions.assertFalse(a.intersects(
            Cuboid(3L..4, 1L..4, 0L..4)))
        Assertions.assertFalse(a.intersects(
            Cuboid(-1L..-1, 1L..4, 0L..4)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2L..4, 3L..4, 0L..4)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2L..4, -1L..-1, 0L..4)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2L..4, 1L..4, 3L..4)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2L..4, 1L..4, -1L..-1)))
        // @formatter:on
    }

    @Test
    fun intersection() {
        val a = Cuboid(0L..2, 0L..2, 0L..2)
        val b = Cuboid(2L..4, 1L..4, 0L..4)
        val intersection = Cuboid(2L..2, 1L..2, 0L..2)
        Assertions.assertEquals(intersection, a.intersection(b))
        Assertions.assertEquals(intersection, b.intersection(a))
    }

}
