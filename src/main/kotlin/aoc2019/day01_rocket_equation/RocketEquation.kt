package aoc2019.day01_rocket_equation

import kotlin.math.max

fun main() {
    util.solve(3336439, ::partOne)
//    util.solve(6, ::partTwo)
}

fun partOne(input: String) = input
    .lines()
    .map(String::toInt)
    .sumOf { max(0, it / 3 - 2) }

fun partTwo(input: String) = input.trim().length

