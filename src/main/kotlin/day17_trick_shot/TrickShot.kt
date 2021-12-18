package day17_trick_shot

import geom2d.Point
import geom2d.Rect
import kotlin.math.abs

fun main() {
    util.solve(11781, ::partOne)
    util.solve(::partTwo)
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

fun partOne(input: String): Long {
    val target = input.toRect()
//    // this is the (well, 'a') dx to use for a final plummet
//    val dx = countForever()
//        .runningFold(Pair(0, 0)) { p, n ->
//            Pair(n, p.second + n)
//        }
//        .dropWhile { it.second < target.x1 }
//        .first()
//        .first
    val dy = abs(target.y1) - 1
    return (1..dy).sum()
}

// "target area: x=1..2, y=3..4" => Rect(1, 3, 2, 4)
fun String.toRect() =
    split(":")[1]
        .split(",")
        .map { it.split("=")[1] }
        .map { it.split("..").map(String::toLong) }
        .let {
            Rect(it[0][0], it[1][0], it[0][1], it[1][1])
        }

fun partTwo(input: String) = input.trim().length
