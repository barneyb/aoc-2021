package aoc2019.day04_secure_container

fun main() {
    util.solve(1716, ::partOne)
    util.solve(::partTwo)
}

internal fun Int.isValidPassword(): Boolean {
    if (this < 100_000 || this > 999_999) return false
    var next = mod(10)
    var n = div(10)
    var hasDouble = false
    for (i in 4 downTo 0) {
        val curr = n % 10
        if (curr > next) {
            return false
        }
        if (curr == next) {
            hasDouble = true
        }
        next = curr
        n /= 10
    }
    return hasDouble
}

fun partOne(input: String): Int {
    val (start, end) = input.lines()
        .first()
        .split("-")
        .map { it.toInt() }
    return (start..end).count(Int::isValidPassword)
}

fun partTwo(input: String) = input.trim().length
