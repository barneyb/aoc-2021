package aoc2015.day05_intern_elves

fun main() {
    util.solve(236, ::partOne)
    util.solve(51, ::partTwo)
}

private fun String.isNice(): Boolean {
    var vowelCount = 0
    var prev = '\u0000'
    var double = false
    forEach { c ->
        when (c) {
            'a', 'e', 'i', 'o', 'u' -> ++vowelCount
            'b' -> if (prev == 'a') return false
            'd' -> if (prev == 'c') return false
            'q' -> if (prev == 'p') return false
            'y' -> if (prev == 'x') return false
        }
        if (c == prev) {
            double = true
        }
        prev = c
    }
    return vowelCount >= 3 && double
}

fun partOne(input: String) = input
    .lines()
    .count { it.isNice() }

private var RE_REPEATED_PAIR = Regex(".*(..).*\\1.*")
private var RE_SEPARATED_REPEAT = Regex(".*(.).\\1.*")

fun partTwo(input: String) = input
    .lines()
    .count {
        RE_REPEATED_PAIR.matches(it) && RE_SEPARATED_REPEAT.matches(it)
    }
