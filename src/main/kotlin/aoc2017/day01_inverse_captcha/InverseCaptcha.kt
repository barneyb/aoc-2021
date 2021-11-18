package aoc2017.day01_inverse_captcha

fun main() {
    util.solve(1089, ::partOne)
    util.solve(1156, ::partTwo)
}

fun partOne(input: String): Int {
    var prev = input[input.length - 1]
    var sum = 0
    for (c in input) {
        if (c == prev) {
            sum += c.digitToInt()
        }
        prev = c
    }
    return sum
}

fun partTwo(input: String): Int {
    val halfLen = input.length / 2
    return 2 * input.subSequence(0, halfLen)
        .filterIndexed { i, c -> c == input[i + halfLen] }
        .sumOf(Char::digitToInt)
}
