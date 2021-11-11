package aoc2019.day01_rocket_equation

import kotlin.math.max

fun main() {
    util.solve(3336439, ::partOne)
    util.solve(5001791, ::partTwo)
}

fun fuelForMass(m: Int) =
    max(0, m / 3 - 2)

fun partOne(input: String) = input
    .lines()
    .map(String::toInt)
    .sumOf(::fuelForMass)

fun partTwo(input: String): Int {
    val result = mutableListOf<Int>()
    input
        .lines()
        .map(String::toInt)
        .forEach {
            var n = it
            while (n > 0) {
                n = fuelForMass(n)
                result.add(n)
            }
        }
    return result.sum()
}

