package aoc2020.day01_report_repair

fun main() {
    util.solve(73371, ::partOne)
    util.solve(::partTwo)
}

private const val TARGET = 2020

fun partOne(input: String): Int {
    val set = hashSetOf<Int>()
    input
        .lines()
        .map(Integer::parseInt)
        .forEach {
            val rest = TARGET - it
            if (set.contains(rest)) {
                return rest * it
            }
            set.add(it)
        }
    throw IllegalStateException("No pair found?!")
}

fun partTwo(input: String) = input.trim().length
