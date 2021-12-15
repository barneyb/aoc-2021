package day14_extended_polymerization

import histogram.Histogram
import histogram.count
import histogram.histogramOf
import histogram.mutableHistogramOf
import util.extent
import util.length

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
    val pairHist = (1..steps).fold(initial) { hist, step ->
        mutableHistogramOf<String>().apply {
            for ((k, v) in hist) {
                rules[k]!!.forEach {
                    count(it, v)
                }
            }
        }
    }
    val charHist: Histogram<Char> = mutableHistogramOf<Char>().apply {
        count(template.first())
        count(template.last())
        for ((p, n) in pairHist) {
            count(p[0], n)
            count(p[1], n)
        }
    }
    val extent = charHist.values.extent()
    if (extent.length % 2L == 1L) throw IllegalStateException("got an odd number?")
    return extent.length / 2
}

fun partTwo(input: String) =
    solve(input, 40)
