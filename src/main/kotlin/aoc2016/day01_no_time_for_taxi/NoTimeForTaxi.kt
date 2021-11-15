package aoc2016.day01_no_time_for_taxi

import geom2d.Heading
import geom2d.Turn

fun main() {
    util.solve(288, ::partOne)
    util.solve(111, ::partTwo)
}

fun partOne(input: String) = input.toPairs()
    .fold(Heading.ORIGIN) { curr, (t, n) ->
        curr.turn(t).step(n)
    }
    .location
    .manhattanDistance()

private fun String.toPairs() = split(",")
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

fun partTwo(input: String): Long {
    val origin = Heading.ORIGIN
    val visited = hashSetOf(origin.location)
    input.toPairs()
        .fold(origin) { curr, (t, n) ->
            var next = curr.turn(t)
            for (i in 0 until n) {
                next = next.step()
                if (!visited.add(next.location)) {
                    return next.manhattanDistance()
                }
            }
            next
        }
    throw IllegalStateException("huh?")
}

