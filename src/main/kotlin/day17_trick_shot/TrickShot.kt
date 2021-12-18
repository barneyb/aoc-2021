package day17_trick_shot

import geom2d.Point
import geom2d.Rect
import util.countForever
import kotlin.math.abs

/**
 * More analysis; no code required. The probe has to go up and then come back
 * down at the same rate (the `y` velocity changes at a constant rate). Thus,
 * the target area's bottom y-coordinate is all you need to know.
 *
 * Like Seven Segment Search (#8), solving part one the expedient way appears
 * valueless for part two. Not true, however. Part one's answer is one of the
 * bounds for part two's search space. Finding the x-dimension's bound is
 * different (it doesn't have a constant rate), but not _much_ different. An
 * exhaustive search of this constrained domain is well within reach, though
 * further constraining is possible.
 */
fun main() {
    util.solve(11781, ::partOne)
    util.solve(4531, ::partTwo)
}

data class Probe(val pos: Point, val vel: Point) {

    constructor(dx: Long, dy: Long) : this(Point.ORIGIN, Point(dx, dy))

    fun step() =
        Probe(
            Point(pos.x + vel.x, pos.y + vel.y),
            Point(
                vel.x.let {
                    when {
                        it == 0L -> 0
                        it < 0 -> it + 1
                        it > 0 -> it - 1
                        else -> throw IllegalStateException("Huh? $it is non-zero, non-negative, and non-positive?")
                    }
                },
                vel.y - 1
            )
        )
}

fun partOne(input: String) =
    (1 until abs(input.toRect().y1)).sum()

// "target area: x=1..2, y=3..4" => Rect(1, 3, 2, 4)
fun String.toRect() =
    split(":")[1]
        .split(",")
        .map { it.split("=")[1] }
        .map { it.split("..").map(String::toLong) }
        .let {
            Rect(it[0][0], it[1][0], it[0][1], it[1][1])
        }

fun partTwo(input: String): Int {
    val target = input.toRect()
    // this is the smallest possible dx (use for a final plummet)
    val minDx = countForever()
        .runningFold(Pair(0, 0)) { p, n ->
            Pair(n, p.second + n)
        }
        .dropWhile { it.second < target.x1 }
        .first()
        .first
    // this is the largest possible dy (a final plummet)
    val maxDy = abs(target.y1) - 1
    // every target area coord can be hit, so those are the other bounds...
    return (minDx..target.x2).sumOf { dx ->
        (target.y1..maxDy).count { dy ->
            var curr = Probe(dx, dy)
            while (true) {
                curr = curr.step()
                if (target.contains(curr.pos)) {
                    // we hit it!
                    return@count true
                }
                if (curr.pos.x > target.x2 || curr.pos.y < target.y1) {
                    // we're past it
                    return@count false
                }
            }
            // Kotlin refuses to compile without this unreachable code?!
            @Suppress("UNREACHABLE_CODE", "ThrowableNotThrown")
            throw IllegalStateException("Never to to/past target area?")
        }
    }
}
