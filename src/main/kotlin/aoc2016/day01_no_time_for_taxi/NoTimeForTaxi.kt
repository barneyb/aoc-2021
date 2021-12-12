package aoc2016.day01_no_time_for_taxi

import geom2d.Dir
import geom2d.Point

fun main() {
    util.solve(288, ::partOne)
    util.solve(111, ::partTwo)
}

data class Heading(val location: Point, val dir: Dir) {

    companion object {
        val ORIGIN = Heading(Point.ORIGIN, Dir.NORTH)
    }

    fun turn(turn: Turn) =
        copy(dir = dir.turn(turn))

    fun step(steps: Long = 1) =
        copy(location = location.step(dir, steps))

    fun manhattanDistance() =
        location.manhattanDistance()

}

fun Char.toTurn() = when (this) {
    'S' -> Turn.STRAIGHT
    'R' -> Turn.RIGHT
    'A' -> Turn.AROUND
    'L' -> Turn.LEFT
    else -> throw IllegalArgumentException("Unknown '$this' turn")
}

enum class Turn {
    STRAIGHT, RIGHT, AROUND, LEFT
}

fun Dir.turn(turn: Turn) = when (turn) {
    Turn.STRAIGHT -> this
    Turn.RIGHT -> when (this) {
        Dir.NORTH -> Dir.EAST
        Dir.EAST -> Dir.SOUTH
        Dir.SOUTH -> Dir.WEST
        Dir.WEST -> Dir.NORTH
    }
    Turn.AROUND -> when (this) {
        Dir.NORTH -> Dir.SOUTH
        Dir.EAST -> Dir.WEST
        Dir.SOUTH -> Dir.NORTH
        Dir.WEST -> Dir.EAST
    }
    Turn.LEFT -> when (this) {
        Dir.NORTH -> Dir.WEST
        Dir.EAST -> Dir.NORTH
        Dir.SOUTH -> Dir.EAST
        Dir.WEST -> Dir.SOUTH
    }
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
            it[0].toTurn(),
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

