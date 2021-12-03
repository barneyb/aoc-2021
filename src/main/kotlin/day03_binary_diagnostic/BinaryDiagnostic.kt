package day03_binary_diagnostic

import util.countForever

fun main() {
    util.solve(3687446, ::partOne)
    util.solve(4406844, ::partTwo)
}

fun partOne(input: String): Long {
    val freqs = IntArray(input.lineSequence().first().length)
    for (line in input.lineSequence()) {
        line.forEachIndexed { i, c ->
            freqs[i] += if (c == '1') 1 else -1
        }
    }
    var gamma = 0L
    var epsilon = 0L
    freqs.forEach { c ->
        gamma = gamma shl 1
        epsilon = epsilon shl 1
        if (c > 0) ++gamma else ++epsilon
    }
    return gamma * epsilon
}

private fun Collection<String>.findRating(bitCrit: (Int) -> Char): Long {
    countForever().fold(this) { lines, i ->
        if (lines.isEmpty()) throw NoSuchElementException()
        if (lines.size == 1) return lines.first().toLong(2)
        val bit = bitCrit(lines.fold(0) { n, line ->
            n + if (line[i] == '1') 1 else -1
        })
        lines.filter {
            it[i] == bit
        }
    }
    throw IllegalStateException("Successfully counted to forever!")
}

fun partTwo(input: String): Long {
    val lines = input.lines()
    val oxygen = lines.findRating { if (it >= 0) '1' else '0' }
    val co2 = lines.findRating { if (it < 0) '1' else '0' }
    return oxygen * co2
}
