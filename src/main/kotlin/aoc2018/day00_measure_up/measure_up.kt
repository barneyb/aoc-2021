package aoc2018.day00_measure_up

fun main() {
    util.solve(16, ::partOne)
    util.solve(14, ::partTwo)
}

fun partOne(input: String) = input.length
fun partTwo(input: String) = input.trim().length
