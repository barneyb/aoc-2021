package aoc2015.day01_almost_lisp

import kotlin.streams.toList

fun main() {
    util.solve(280, ::partOne)
    util.solve(1797, ::partTwo)
}

private fun String.toDeltaStream() = this.chars()
    .map(::parenToDelta)

fun partOne(input: String) = input
    .toDeltaStream()
    .reduce(0, Int::plus)

fun partTwo(input: String): Int {
    val list = input
        .toDeltaStream()
        .toList()
    if (list.size == 1) {
        if (list.first() == -1) {
            return 1
        }
    }
    if (!list.isEmpty()) {
        list.reduceIndexed { idx, prev, it ->
            val floor = prev + it
            if (floor < 0) {
                return idx + 1 // one-based position
            }
            floor
        }
    }
    throw IllegalArgumentException("Basement is never reached")
}

private fun parenToDelta(c: Int) = when (c.toChar()) {
    '(' -> 1
    ')' -> -1
    else -> throw IllegalArgumentException("Unknown '${c}' char")
}
