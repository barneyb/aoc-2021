package aoc2017.day02_corruption_checksum

fun main() {
    util.solve(53978, ::partOne)
    util.solve(314, ::partTwo)
}

private val RE_RUN_OF_SPACES = Regex("\\s+")

private fun checksum(spreadsheet: String, rowChecksum: (List<Int>) -> Int) =
    spreadsheet
        .lines()
        .sumOf {
            rowChecksum(
                it.split(RE_RUN_OF_SPACES)
                    .map(Integer::parseInt)
            )
        }

fun partOne(input: String) =
    checksum(input) {
        val (min, max) = it.fold(
            Pair(Int.MAX_VALUE, Int.MIN_VALUE)
        ) { (min, max), n ->
            Pair(min.coerceAtMost(n), max.coerceAtLeast(n))
        }
        max - min
    }

fun partTwo(input: String) =
    checksum(input) { numbers ->
        numbers.forEachIndexed { i, a ->
            numbers.subList(0, i).forEach { b ->
                if (a > b) {
                    if (a % b == 0) return@checksum a / b
                } else {
                    if (b % a == 0) return@checksum b / a
                }
            }
        }
        throw IllegalStateException("no divisible numbers?!")
    }
