package day06_lanternfish_growth

import histogram.Histogram
import histogram.buildHistogram
import histogram.count
import histogram.total

fun main() {
    util.solve(371_379, ::partOne)
    util.solve(1_674_303_997_472, ::partTwo)
}

fun partOne(input: String) = simulate(input, 80)

private fun String.toCounts() =
    buildHistogram<Int>(9) {
        split(',')
            .map(String::toInt)
            .forEach { count(it, 1) }
    }

private fun nextGeneration(curr: Histogram<Int>) =
    buildHistogram<Int>(9) {
        curr.entries.forEach { (timer, count) ->
            if (timer == 0) {
                count(6, count) // reset
                count(8, count) // birth
            } else {
                count(timer - 1, count) // decrement
            }
        }
    }

fun simulate(input: String, days: Int) =
    simulate(input.toCounts(), days)

fun simulate(initial: Histogram<Int>, days: Int) =
    (1..days)
        .fold(initial) { counts, _ ->
            nextGeneration(counts)
        }
        .total

fun partTwo(input: String) = simulate(input, 256)
