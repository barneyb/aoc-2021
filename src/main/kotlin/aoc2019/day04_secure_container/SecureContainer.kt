package aoc2019.day04_secure_container

fun main() {
    util.solve(1716, ::partOne)
    util.solve(::partTwo)
}

internal fun Int.isValidPassword(): Boolean {
    if (this < 100_000 || this > 999_999) return false
    val s = toString()
    if (s[0] == s[1] || s[1] == s[2] || s[2] == s[3] || s[3] == s[4] || s[4] == s[5]) {
        return s[0] <= s[1] && s[1] <= s[2] && s[2] <= s[3] && s[3] <= s[4] && s[4] <= s[5]
    }
    return false
}

fun partOne(input: String): Int {
    val (start, end) = input.lines()
        .first()
        .split("-")
        .map { it.toInt() }
    return (start..end).count(Int::isValidPassword)
}

fun partTwo(input: String) = input.trim().length
