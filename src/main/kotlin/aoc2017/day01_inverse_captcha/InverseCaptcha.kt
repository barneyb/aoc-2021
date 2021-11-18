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
    var sum = 0
    val halfLen = input.length / 2
    for (i in 0 until halfLen) {
        if (input[i] == input[i + halfLen]) {
            sum += 2 * input[i].digitToInt()
        }
    }
    return sum
}
