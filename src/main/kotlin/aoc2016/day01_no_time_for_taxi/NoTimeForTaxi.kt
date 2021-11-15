package aoc2016.day01_no_time_for_taxi

import geom2d.Dir
import geom2d.Heading
import geom2d.Point
import geom2d.Turn

fun main() {
    util.solve(288, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input
    .split(",")
    .map { it.trim() }
    .map {
        Pair(
            when (it[0]) {
                'R' -> Turn.RIGHT
                'L' -> Turn.LEFT
                else -> throw IllegalArgumentException("Unknown '${it[0]}' turn.")
            },
            it.substring(1, it.length).toLong()
        )
    }
    .fold(Heading(Point.ORIGIN, Dir.NORTH)) { curr, (t, n) ->
        curr.turn(t).step(n)
    }
    .location
    .manhattanDistance()

fun partTwo(input: String) = input.trim().length
