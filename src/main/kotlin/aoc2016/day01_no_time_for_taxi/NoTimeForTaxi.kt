package aoc2016.day01_no_time_for_taxi

fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input.length

fun partTwo(input: String) = input.trim().length
