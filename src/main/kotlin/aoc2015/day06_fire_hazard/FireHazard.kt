package aoc2015.day06_fire_hazard

import geom2d.Point
import java.util.*

fun main() {
    util.solve(543903, ::partOne)
    util.solve(::partTwo)
}

private fun String.toPoint(): Point {
    val dims = split(",")
        .map { it.toLong() }
    return Point(dims[0], dims[1])
}

private fun gridRanges(a: Point, b: Point): Collection<IntRange> {
    return (a.y..b.y).map {
        val offset = 1_000 * it
        (a.x + offset).toInt()..(b.x + offset).toInt()
    }
}

fun partOne(input: String): Int {
    val lights = BitSet(1_000_000)
    input.lines().forEach { line ->
        var words = line.split(" ")
        if (words.first() == "turn") {
            words = words.subList(1, words.size)
        }
        val ranges = gridRanges(
            words[1].toPoint(),
            words[3].toPoint(),
        )
        when (words.first()) {
            "on" -> ranges.forEach { lights.set(it.first, it.last + 1) }
            "off" -> ranges.forEach { lights.clear(it.first, it.last + 1) }
            "toggle" -> ranges.forEach { lights.flip(it.first, it.last + 1) }
            else -> throw IllegalArgumentException("unrecognized instruction '$line'")
        }
    }
    return lights.cardinality()
}

fun partTwo(input: String) = input.trim().length
