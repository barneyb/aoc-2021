package aoc2019.day04_secure_container

fun main() {
    util.solve(1716, ::partOne)
    util.solve(1163, ::partTwo)
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

private fun String.toRange(): IntRange {
    val (start, end) = lines()
        .first()
        .split("-")
        .map { it.toInt() }
    return start..end
}

fun partOne(input: String) =
    input.toRange().count(Int::isValidPassword)

internal fun Int.isMoreValidPassword(): Boolean {
    if (this < 100_000 || this > 999_999) return false
    var next = mod(10)
    var n = div(10)
    var hasDouble = false
    var runLen = 1
    for (i in 4 downTo 0) {
        val curr = n % 10
        if (curr > next) {
            return false
        }
        if (curr == next) {
            ++runLen
        } else {
            if (runLen == 2) {
                hasDouble = true
            }
            runLen = 1
        }
        next = curr
        n /= 10
    }
    if (runLen == 2) {
        hasDouble = true
    }
    return hasDouble
}

fun partTwo(input: String) =
    input.toRange().count(Int::isMoreValidPassword)
