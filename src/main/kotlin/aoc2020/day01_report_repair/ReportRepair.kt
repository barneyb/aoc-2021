package aoc2020.day01_report_repair

fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input.length

fun partTwo(input: String) = input.trim().length
