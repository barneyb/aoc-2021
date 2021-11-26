package aoc2020.day03_toboggan_trajectory

fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input.length

fun partTwo(input: String) = input.trim().length
