package day06_lanternfish_growth

import histogram.Histogram
import histogram.buildHistogram
import histogram.count
import histogram.total
import util.saveTextFile
import java.io.PrintWriter

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
