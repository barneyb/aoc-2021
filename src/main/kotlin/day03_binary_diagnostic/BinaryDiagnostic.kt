package day03_binary_diagnostic

import util.CountedToForeverException
import util.countForever

/**
 * Bit masking is the main idea here, along with avoiding needless redundant
 * processing. Since the gamma and epsilon rates are inverses, computing one
 * directly gives the other.
 *
 * Part two makes the masking more interesting, as there are a lot more ops to
 * perform. It also requires an unknown-endpoint iteration through the bits, to
 * repeatedly filter the collection of numbers.
 */
fun main() {
    util.solve(3687446, ::partOne)
    util.solve(4406844, ::partTwo)
}

fun partOne(input: String): Long {
    val lines = input.lines()
    val freqs = IntArray(lines.first().length)
    for (line in lines) {
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

private fun Collection<Long>.findRating(
    width: Int,
    bitCrit: (Int) -> Boolean
): Long {
    countForever().fold(this) { numbers, i ->
        if (numbers.isEmpty()) throw NoSuchElementException()
        if (numbers.size == 1) return numbers.first()
        val mask = 1L shl (width - i - 1)
        val hot = bitCrit(numbers.fold(0) { n, line ->
            n + if (line and mask != 0L) 1 else -1
        })
        numbers.filter {
            if (it and mask != 0L) hot else !hot
        }
    }
    throw CountedToForeverException()
}

fun partTwo(input: String): Long {
    val lines = input.lines()
    val width = lines.first().length
    val numbers = lines.map { it.toLong(2) }
    val oxygen = numbers.findRating(width) { it >= 0 }
    val co2 = numbers.findRating(width) { it < 0 }
    return oxygen * co2
}
