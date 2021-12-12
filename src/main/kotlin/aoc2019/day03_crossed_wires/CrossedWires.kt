package aoc2019.day03_crossed_wires

import geom2d.Dir
import geom2d.Point
import geom2d.toDir

fun main() {
    util.solve(1084, ::partOne)
    util.solve(9240, ::partTwo)
}

data class Step(val dir: Dir, val n: Long)

private fun String.toStep() =
    Step(
        this.first().toDir(),
        this.substring(1).toLong()
    )

private fun String.toSteps() =
    this.trim()
        .split(",")
        .map(String::toStep)

private fun walkForPositionsAndDistance(steps: List<Step>): Map<Point, Long> {
    // deliberately not including origin, even though the wire starts there,
    // as the theme is "crossed wires" and the shared origin appears like a
    // cross, even though it isn't. So just ignore it here. Fingers crossed.
    val points = mutableMapOf<Point, Long>()
    var distance = 0L
    steps.fold(Point.ORIGIN) { base, s ->
        (0 until s.n).fold(base) { curr, _ ->
            val next = curr.step(s.dir)
            points.putIfAbsent(next, ++distance)
            next
        }
    }
    return points
}

fun partOne(input: String): Long {
    val (a, b) = input
        .lines()
        .map(String::toSteps)
        .map(::walkForPositionsAndDistance)
    return a.keys.intersect(b.keys)
        .minOf(Point::manhattanDistance)
}

fun partTwo(input: String): Long {
    val (a, b) = input
        .lines()
        .map(String::toSteps)
        .map(::walkForPositionsAndDistance)
    return a.keys.intersect(b.keys)
        .minOf { a[it]!! + b[it]!! }
}
