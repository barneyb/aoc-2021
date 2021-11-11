package aoc2019.day03_crossed_wires

import geom2d.Dir
import geom2d.Point
import geom2d.Step

fun main() {
    util.solve(1084, ::partOne)
//    util.solve(6, ::partTwo)
}

private fun String.toStep() =
    Step(
        when (this.first()) {
            'U' -> Dir.NORTH
            'R' -> Dir.EAST
            'D' -> Dir.SOUTH
            'L' -> Dir.WEST
            else -> throw IllegalArgumentException("Unknown '${this.first()}' step direction")
        },
        this.substring(1).toLong()
    )

private fun String.toSteps() =
    this.trim()
        .split(",")
        .map(String::toStep)

private fun walkForPositions(steps: List<Step>): Set<Point> {
    // deliberately not including origin, even though the wire starts there,
    // as the theme is "crossed wires" and the shared origin appears like a
    // cross, even though it isn't. So just ignore it here. Fingers crossed.
    val points = mutableSetOf<Point>()
    steps.fold(Point(0, 0)) { base, s ->
        (0 until s.n).fold(base) { curr, _ ->
            val next = curr.step(s.dir)
            points += next
            next
        }
    }
    return points
}

fun partOne(input: String): Long {
    val (a, b) = input
        .lines()
        .map(String::toSteps)
        .map(::walkForPositions)
    return a.intersect(b)
        .minOf(Point::mannDist)
}

fun partTwo(input: String) = input.trim().length

