package day06_lanternfish_growth

import histogram.buildHistogram
import histogram.count
import histogram.total

fun main() {
    util.solve(371_379, ::partOne)
    util.solve(1_674_303_997_472, ::partTwo)
}

fun partOne(input: String) = simulate(input, 80)

fun simulate(input: String, days: Int): Long {
    val initial = buildHistogram<Int>(9) {
        input.split(',')
            .map(String::toInt)
            .forEach { count(it, 1) }
    }
    return (1..days)
        .fold(initial) { fish, _ ->
            buildHistogram(9) {
                fish.entries.forEach { (timer, count) ->
                    if (timer == 0) {
                        count(6, count) // reset
                        count(8, count) // birth
                    } else {
                        count(timer - 1, count) // decrement
                    }
                }
            }
        }
        .total
}

fun partTwo(input: String) = simulate(input, 256)
