package day01_sonar_sweep

import java.util.*

fun main() {
    util.solve(1616, ::partOneFold)
    util.solve(1616, ::partOneZipSeq)
    util.solve(1616, ::partOneZip)
    util.solve(1616, ::partOneLoop)
    util.solve(1645, ::partTwo)
    util.solve(1645, ::partTwoZip)
}

fun partOneFold(input: String) =
    input
        .lineSequence()
        .map { it.toInt() }
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

fun partOneZipSeq(input: String) =
    input
        .lineSequence()
        .map { it.toInt() }
        .zipWithNext()
        .count { (a, b) -> a < b }

fun partOneZip(input: String) =
    input
        .lines()
        .map { it.toInt() }
        .zipWithNext()
        .filter { (a, b) -> a < b }
        .size

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
        .map { it.toInt() }
        .windowed(3)
        .map { it.sum() }
        .zipWithNext()
        .count { (a, b) -> a < b }