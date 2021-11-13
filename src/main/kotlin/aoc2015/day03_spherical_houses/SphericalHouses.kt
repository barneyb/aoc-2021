package aoc2015.day03_spherical_houses

import geom2d.Dir
import geom2d.Point
import kotlin.streams.toList

fun main() {
    util.solve(2565, ::partOne)
    util.solve(2639, ::partTwo)
}

private fun String.toDirList() = this
    .chars()
    .mapToObj(::charToDir)
    .toList()

private fun charToDir(c: Int) = when (c.toChar()) {
    '^' -> Dir.NORTH
    '>' -> Dir.EAST
    'v' -> Dir.SOUTH
    '<' -> Dir.WEST
    else -> throw IllegalArgumentException("Unknown '$c' direction")
}

fun partOne(input: String): Int {
    val origin = Point(0, 0)
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
    val origin = Point(0, 0)
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

