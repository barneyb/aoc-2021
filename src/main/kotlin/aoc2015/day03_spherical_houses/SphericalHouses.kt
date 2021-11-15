package aoc2015.day03_spherical_houses

import geom2d.Point
import geom2d.toDir
import kotlin.streams.toList

fun main() {
    util.solve(2565, ::partOne)
    util.solve(2639, ::partTwo)
}

private fun String.toDirList() = this
    .chars()
    .mapToObj { it.toChar().toDir() }
    .toList()

fun partOne(input: String): Int {
    val origin = Point.ORIGIN
    val points = mutableSetOf(origin)
    input.toDirList()
        .fold(origin) { curr, dir ->
            val next = curr.step(dir)
            points += next
            next
        }
    return points.size
}

fun partTwo(input: String): Int {
    val origin = Point.ORIGIN
    val points = mutableSetOf(origin)
    input.toDirList()
        .foldIndexed(Pair(origin, origin)) { i, (s, rs), dir ->
            if (i % 2 == 0) {
                val next = s.step(dir)
                points += next
                Pair(next, rs)
            } else {
                val next = rs.step(dir)
                points += next
                Pair(s, next)
            }
        }
    return points.size
}

