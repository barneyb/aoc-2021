package day06_lanternfish_growth

import histogram.Histogram
import histogram.buildHistogram
import histogram.count
import histogram.total
import util.saveTextFile
import java.io.PrintWriter

/**
 * The first analysis day! Implementing a solution based on the description of
 * the "business" problem will get it done, but hardly a good approach. Mapping
 * business problems onto a computer domain is the essence of programming, and
 * today's the day! In many ways, today is another day one: a very simple
 * problem, just "can you do analysis" vs "can you read a text file". Except it
 * isn't actually required...
 *
 * Part two makes it required. There is no possible way to get the second star
 * with a naive - though correct - implementation of part one. All "timer 2"
 * fish are the same class of fish, and can be treated uniformly. Identifying
 * the right types in the business domain is crucial to create good software.
 */
fun main() {
    util.solve(371_379, ::partOne)
    util.solve(1_674_303_997_472, ::partTwo)
    saveTextFile(::csv, "csv")
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
    trace(initial, days)
        .last()
        .total

private fun trace(initial: Histogram<Int>, days: Int) =
    (1..days)
        .runningFold(initial) { counts, _ ->
            nextGeneration(counts)
        }

//fun simulate(initial: Histogram<Int>, days: Int): Long {
//    var curr = initial
//    (1..days).forEach { day ->
//        curr = nextGeneration(curr)
//    }
//    return curr.total
//}

//fun simulate(initial: Histogram<Int>, days: Int): Long {
//    var curr = initial
//    repeat(days) {
//        curr = nextGeneration(curr)
//    }
//    return curr.total
//}

fun partTwo(input: String) = simulate(input, 256)

private fun csv(input: String, out: PrintWriter) {
    out.println("t,value,count")
    trace(input.toCounts(), 256)
        .forEachIndexed { t, hist ->
            hist.forEach { (v, c) ->
                out.println("$t,$v,$c")
            }
        }
}
