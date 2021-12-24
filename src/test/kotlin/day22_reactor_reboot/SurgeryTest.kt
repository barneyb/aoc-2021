package day22_reactor_reboot

import geom.Cuboid
import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import org.junit.jupiter.api.Test
import util.printBoxed
import kotlin.test.assertEquals

private const val CHARS =
    "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"

private val EMPTY = '.'

private fun render(cuboids: Collection<Cuboid>) {
    fun Cuboid.contains(p: Point) =
        dims[0].contains(p.x) && dims[1].contains(p.y)

    printBoxed(Rect(0, 0, 18, 14).mirrorY().toAsciiArt {
        var ch = EMPTY
        for ((idx, cube) in cuboids.withIndex()) {
            if (cube.contains(it)) {
                if (ch != EMPTY) {
                    // doubled!
                    ch = '█'
                    break
                }
                ch = CHARS[idx]
            }
        }
        ch.toString()
    })
}

private fun render(vararg cuboids: Cuboid) =
    render(cuboids.asList())

class SurgeryTest {

    // @formatter:off
    private val A = Cuboid( 5..11L,  3.. 8L)
    // corners
    private val b = Cuboid( 2.. 5L,  7.. 9L)
    private val d = Cuboid( 9..13L,  7.. 9L)
    private val h = Cuboid( 2.. 5L,  1.. 3L)
    private val j = Cuboid(10..14L,  1.. 3L)
    // middle-of-edge
    private val c = Cuboid( 7.. 7L,  7..10L)
    private val e = Cuboid( 2.. 5L,  5.. 5L)
    private val g = Cuboid(10..12L,  5.. 5L)
    private val i = Cuboid( 7.. 8L,  1.. 3L)
    // center
    private val f = Cuboid( 7.. 8L,  5.. 5L)
    // non-overlapping
    private val k = Cuboid(17..18L, 13..14L)
    // larger in every direction
    private val l = Cuboid( 4..15L,  2..11L)
    // edge and both corners
    private val m = Cuboid( 3..14L,  8..10L)
    private val n = Cuboid( 1.. 5L,  8.. 8L)
    private val o = Cuboid( 3..12L,  3.. 4L)
    private val p = Cuboid(11..13L,  1.. 8L)
    // single exact corner
    private val q = Cuboid( 5.. 5L,  8.. 8L)
    private val s = Cuboid(11..11L,  8.. 8L)
    private val v = Cuboid( 5.. 5L,  3.. 3L)
    private val x = Cuboid(11..11L,  3.. 3L)
    // single exact middle-of-edge
    private val r = Cuboid( 8.. 8L,  8.. 8L)
    private val t = Cuboid( 5.. 5L,  5.. 5L)
    private val u = Cuboid(11..11L,  5.. 5L)
    private val w = Cuboid( 8.. 8L,  3.. 3L)
    // @formatter:on

    @Test
    fun renderSanity() {
        render(A, b, f, k, p)
    }

    @Test
    fun surgeryAb() {
        val s = performSurgery(A, b)
        render(s.all)
        assertEquals(1, s.fromBoth.size)
        assertEquals(Cuboid(5..5L, 7..8L), s.fromBoth.first())
        assertEquals(2, s.fromOne.size)
        assertEquals(Cuboid(6..11L, 3..8L), s.fromOne.first())
        assertEquals(Cuboid(5..5L, 3..6L), s.fromOne.last())
        assertEquals(2, s.fromTwo.size)
        assertEquals(Cuboid(2..4L, 7..9L), s.fromTwo.first())
        assertEquals(Cuboid(5..5L, 9..9L), s.fromTwo.last())
    }

    @Test
    fun surgeryAk_noOverlap() {
        val s = performSurgery(A, k)
        render(s.all)
        assertEquals(0, s.fromBoth.size)
        assertEquals(1, s.fromOne.size)
        assertEquals(A, s.fromOne.first())
        assertEquals(1, s.fromTwo.size)
        assertEquals(k, s.fromTwo.first())
    }

    @Test
    fun surgeryAl_fullyEnveloping() {
        val s = performSurgery(A, l)
        render(s.all)
        assertEquals(1, s.fromBoth.size)
        assertEquals(A, s.fromBoth.first())
        assertEquals(4, s.fromTwo.size)
        assertEquals(Cuboid(4..4L, 2..11L), s.fromTwo[0])
        assertEquals(Cuboid(12..15L, 2..11L), s.fromTwo[1])
        assertEquals(Cuboid(5..11L, 2..2L), s.fromTwo[2])
        assertEquals(Cuboid(5..11L, 9..11L), s.fromTwo[3])
    }

}
