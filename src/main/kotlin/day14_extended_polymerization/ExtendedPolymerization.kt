package day14_extended_polymerization

import histogram.histogramOf

fun main() {
    util.solve(2891, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Long {
    val lines = input.lines()
    val template = lines.first()
    val rules = lines.drop(2)
        .map { it.split(" -> ") }
        .associate { it.first().let { Pair(it[0], it[1]) } to it.last() }
    val result = (1..10).fold(template) { tmpl, _ ->
        tmpl[0] + tmpl
            .zipWithNext()
            .joinToString(separator = "") {
                val c = rules[it]!!
                "$c${it.second}"
            }
    }
    val hist = histogramOf(result.asSequence())
    return hist.values.maxOrNull()!! - hist.values.minOrNull()!!
}

fun partTwo(input: String) = input.trim().length
