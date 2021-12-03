package aoc2018.day01_chronal_calibration

import util.CountedToForeverException
import util.countForever

fun main() {
    util.solve(513, ::partOne)
    util.solve(287, ::partTwo)
}

fun partOne(input: String) = input
    .lines()
    .sumOf { it.trim().toInt() }

fun partTwo(input: String): Int {
    val changes = input
        .lines()
        .map { it.trim().toInt() }
    val visited = hashSetOf<Int>()
    visited.add(0)
    countForever().fold(0) { freq, _ ->
        changes.fold(freq) { f, c ->
            val next = f + c
            if (!visited.add(next)) {
                return next
            }
            next
        }
    }
    throw CountedToForeverException()
}

