package aoc2018.day01_chronal_calibration

fun main() {
    util.solve(513, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input
    .lines()
    .sumOf { it.trim().toInt() }

fun partTwo(input: String) = input.trim().length

