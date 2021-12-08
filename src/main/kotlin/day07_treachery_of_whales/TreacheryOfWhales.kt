package day07_treachery_of_whales

import java.util.*
import kotlin.math.abs

fun main() {
    util.solve(337833, ::partOne)
    util.solve(96678050, ::partTwo)
}

fun partOne(input: String) =
    search(input) { it }

private fun search(input: String, costToMove: (Int) -> Int): Int {
    val crabs = input
        .split(",")
        .map(String::toInt)
    val best = crabs
        .average()
        .toInt()
    val queue: Queue<Int> = LinkedList()
    queue.add(best)
    while (queue.isNotEmpty()) {
        val pivot = queue.remove()
        val curr = crabs.sumOf { abs(costToMove(it - pivot)) }
        val down = crabs.sumOf { abs(costToMove(it - (pivot - 1))) }
        val up = crabs.sumOf { abs(costToMove(it - (pivot + 1))) }
        if (curr <= down && curr <= up) {
            return curr
        } else if (down < curr) {
            queue.add(pivot - 1)
        } else {
            queue.add(pivot + 1)
        }
    }
    throw IllegalStateException("No minumum found?!")
}

fun partTwo(input: String) =
    search(input) { (1..abs(it)).sum() }
