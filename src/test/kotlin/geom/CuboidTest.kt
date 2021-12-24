package geom

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CuboidTest {

    @Test
    fun size() {
        Assertions.assertEquals(1, Cuboid(0..0L).size)
        Assertions.assertEquals(1, Cuboid(0..0L, 0..0L, 0..0L).size)
        Assertions.assertEquals(27, Cuboid(0..2L, 0..2L, 0..2L).size)
        Assertions.assertEquals(0, Cuboid(0..2L, 0..2L, EMPTY_RANGE).size)
    }

    @Test
    fun intersect() {
        val a = Cuboid(0..2L, 0..2L, 0..2L)
        // @formatter:off
        // identical
        Assertions.assertTrue(a.intersects(
            Cuboid(0..2L, 0..2L, 0..2L)))
        // inside
        Assertions.assertTrue(a.intersects(
            Cuboid(1..1L, 1..1L, 1..1L)))
        // corner
        Assertions.assertTrue(a.intersects(
            Cuboid(2..3L, 2..3L, 2..3L)))
        // edge
        Assertions.assertTrue(a.intersects(
            Cuboid(2..3L, 1..1L, 2..3L)))
        // face
        Assertions.assertTrue(a.intersects(
            Cuboid(2..3L, 1..1L, 1..1L)))
        // @formatter:on
    }

    @Test
    fun contains() {
        val a = Cuboid(0..2L, 0..2L, 0..2L)
        // @formatter:off
        // identical
        Assertions.assertTrue(a.contains(
            Cuboid(0..2L, 0..2L, 0..2L)))
        // inside
        Assertions.assertTrue(a.contains(
            Cuboid(1..1L, 1..1L, 1..1L)))
        // corner
        Assertions.assertFalse(a.contains(
            Cuboid(2..3L, 2..3L, 2..3L)))
        // edge
        Assertions.assertFalse(a.contains(
            Cuboid(2..3L, 1..1L, 2..3L)))
        // face
        Assertions.assertFalse(a.contains(
            Cuboid(2..3L, 1..1L, 1..1L)))
        // @formatter:on
    }

    @Test
    fun doesntIntersect() {
        val a = Cuboid(0..2L, 0..2L, 0..2L)
        // @formatter:off
        Assertions.assertFalse(a.intersects(
            Cuboid(3..4L, 1..4L, 0..4L)))
        Assertions.assertFalse(a.intersects(
            Cuboid(-1..-1L, 1..4L, 0..4L)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2..4L, 3..4L, 0..4L)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2..4L, -1..-1L, 0..4L)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2..4L, 1..4L, 3..4L)))
        Assertions.assertFalse(a.intersects(
            Cuboid(2..4L, 1..4L, -1..-1L)))
        // @formatter:on
    }

    @Test
    fun intersection() {
        val a = Cuboid(0..2L, 0..2L, 0..2L)
        val b = Cuboid(2..4L, 1..4L, 0..4L)
        val intersection = Cuboid(2..2L, 1..2L, 0..2L)
        Assertions.assertEquals(intersection, a.intersection(b))
        Assertions.assertEquals(intersection, b.intersection(a))
    }

}
