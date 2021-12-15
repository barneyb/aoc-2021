package day14_extended_polymerization

import histogram.count
import histogram.histogramOf
import histogram.mutableHistogramOf

fun main() {
    util.solve(2891, ::partOne)
    // 975.249μs initially
    // 188.474μs w/ part two
    util.solve(4607749009683, ::partTwo)
}

fun partOne(input: String) =
    solve(input, 10)

internal fun solve(input: String, steps: Int): Long {
    val lines = input.lines()
    val template = lines.first()
    val rules = lines.drop(2)
        .map { it.split(" -> ") }
        .associate { rule ->
            rule.first()
                .let { it to listOf(it[0] + rule.last(), rule.last() + it[1]) }
        }
    val initial = histogramOf(template
        .zipWithNext { a, b -> "$a$b" })
    val hist = (1..steps).fold(initial) { hist, step ->
        val next = mutableHistogramOf<String>()
        for ((k, v) in hist) {
            rules[k]!!.forEach {
                next.count(it, v)
            }
        }
        next
    }
    val chars = mutableHistogramOf<Char>()
    chars.count(template.first())
    chars.count(template.last())
    for ((p, n) in hist) {
        chars.count(p[0], n)
        chars.count(p[1], n)
    }
    val diff = chars.values.maxOrNull()!! - chars.values.minOrNull()!!
    if (diff % 2L == 1L) throw IllegalStateException("got an odd number?")
    return diff / 2
}

fun partTwo(input: String) =
    solve(input, 40)
