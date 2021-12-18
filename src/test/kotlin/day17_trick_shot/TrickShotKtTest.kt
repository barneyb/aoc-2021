package day17_trick_shot

import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val WORKED_EXAMPLE = """
    target area: x=20..30, y=-10..-5
""".trimIndent()

internal class TrickShotKtTest {

    @Test
    fun drawExample() {
        val target = WORKED_EXAMPLE.toRect()
        println("target: $target")
        val locations = mutableListOf<Probe>()
            .apply {
//                var curr = Probe(7, 2)
//                var curr = Probe(6, 3)
//                var curr = Probe(9, 0)
//                var curr = Probe(17, -4) // miss
                var curr = Probe(6, 9) // best (dx=7 works too)
//                var curr = Probe(6, 8)
                add(curr)
                while (true) {
                    curr = curr.step()
                    add(curr)
                    if (curr.pos.x > target.x2 || curr.pos.y < target.y1) break
                    if (target.contains(curr.pos)) break
                }
            }
            .map(Probe::pos)
            .toSet()
        println("locations: $locations")
        locations
            .fold(target, Rect::expandedTo)
            .also(::println)
            .mirrorY()
            .toAsciiArt {
                when {
                    it == Point.ORIGIN -> "S"
                    locations.contains(it) -> "#"
                    target.contains(it) -> "T"
                    else -> "."
                }
            }
            .also(::println)
    }

    @Test
    fun partOne() {
        assertEquals(45, partOne(WORKED_EXAMPLE))
    }

    @kotlin.test.Ignore // todo: reinstate when ready!
    @Test
    fun partTwo() {
        assertEquals(0, partTwo(WORKED_EXAMPLE))
    }

}
