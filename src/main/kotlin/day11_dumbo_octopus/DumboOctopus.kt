package day11_dumbo_octopus

fun main() {
    util.solve(::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) = input.length

fun partTwo(input: String) = input.trim().length
