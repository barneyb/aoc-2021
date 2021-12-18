package day01_sonar_sweep

import util.saveTextFile
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * Classic day one: prove you can read in a text file, get numeric values out of
 * it, and then loop over them.
 *
 * Part two requires the a windowed loop, instead of treating each item
 * separately. Kotlin has a library function which can be simply injected into
 * part one's solution without changing anything else (consider [partOneZip] vs
 * [partTwoZip]).
 */
fun main() {
    util.solve(1616, ::partOneFold)
    util.solve(1616, ::partOneLoop)
    util.solve(1616, ::partOneZip)
    util.solve(1645, ::partTwo)
    util.solve(1645, ::partTwoZip)
    saveTextFile(::csv, "csv")
}

private fun depthsOne(depths: Sequence<Int>) =
    depths

fun partOneFold(input: String) =
    depthsOne(
        input
            .lineSequence()
            .map(String::toInt)
    )
        .fold(Pair(0, Int.MAX_VALUE)) { (n, prev), it ->
            Pair(if (it > prev) n + 1 else n, it)
        }
        .first

fun partOneLoop(input: String): Int {
    var prev = Int.MAX_VALUE
    var count = 0
    for (it in input.lineSequence()) {
        val n = it.toInt()
        if (n > prev) {
            count += 1
        }
        prev = n
    }
    return count
}

fun partOneZip(input: String) =
    input
        .lineSequence()
        .map(String::toInt)
        .zipWithNext()
        .count { (a, b) -> a < b }

fun partTwo(input: String): Int {
    var prev = Int.MAX_VALUE
    var curr = 0
    var count = 0
    val buffer: Queue<Int> = LinkedList()
    for (it in input.lineSequence()) {
        val n = it.toInt()
        buffer.add(n)
        curr += n
        if (buffer.size > 3) {
            curr -= buffer.remove()
        }
        if (buffer.size == 3) {
            if (curr > prev) {
                count += 1
            }
            prev = curr
        }
    }
    return count
}

fun partTwoZip(input: String) =
    input
        .lineSequence()
        .map(String::toInt)
        .windowed(size = 3, transform = List<Int>::sum) // new vs partOneZip
        .zipWithNext()
        .count { (a, b) -> a < b }

private fun depthsTwo(raw: Sequence<Int>) =
    raw
        .windowed(3, partialWindows = true)
        .map { it.sum() / it.size }

fun csv(input: String): String {
    val sw = StringWriter()
    val out = PrintWriter(sw)
    csv(input, out)
    out.close()
    return sw.toString()
}

private fun csv(input: String, out: PrintWriter) {
    out.println("pos,depth,smooth_depth")
    val raw = input
        .lineSequence()
        .map { it.toInt() }
    depthsOne(raw)
        .zip(depthsTwo(raw))
        .forEachIndexed { i, (r, s) ->
            out.println("$i,$r,$s")
        }
}
