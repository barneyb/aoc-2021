package day08_seven_segment

fun main() {
    util.solve(390, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String) =
    input
        .lineSequence()
        .map { it.substring(61) }
        .flatMap { it.split(' ') }
        .map(String::length)
        .count { it == 2 || it == 3 || it == 4 || it == 7 }

fun partTwo(input: String) = input.trim().length
