package aoc2018.day00_measure_up

fun main() {
    val input = util.getInput()
    util.answer(partOne(input))
    util.answer(partTwo(input))
}

fun partOne(input: String) = input.length
fun partTwo(input: String) = input.trim().length
