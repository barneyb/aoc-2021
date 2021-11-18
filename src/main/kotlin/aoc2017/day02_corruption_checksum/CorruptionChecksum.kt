package aoc2017.day02_corruption_checksum

fun main() {
    util.solve(53978, ::partOne)
    util.solve(::partTwo)
}

private val RE_RUN_OF_SPACES = Regex("\\s+")

fun partOne(input: String) = input
    .lines()
    .map {
        it.split(RE_RUN_OF_SPACES)
            .map(Integer::parseInt)
            .fold(Pair(Int.MAX_VALUE, Int.MIN_VALUE)) { (min, max), n ->
                Pair(min.coerceAtMost(n), max.coerceAtLeast(n))
            }

    }
    .sumOf { (min, max) -> max - min }

fun partTwo(input: String) = input.trim().length
