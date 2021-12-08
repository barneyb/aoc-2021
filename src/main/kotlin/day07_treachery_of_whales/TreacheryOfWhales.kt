package day07_treachery_of_whales

import kotlin.math.abs

fun main() {
    util.solve(337833, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
    val crabs = input
        .split(",")
        .map(String::toInt)
    var pivot = crabs
        .average()
        .toInt()
    while (true) {
        val curr = crabs.sumOf { abs(it - pivot) }
        val down = crabs.sumOf { abs(it - (pivot - 1)) }
        val up = crabs.sumOf { abs(it - (pivot + 1)) }
        if (curr < down || curr > up) {
            return curr
        } else if (down < curr) {
            --pivot
        } else {
            ++pivot
        }
    }
}

fun partTwo(input: String) = input.trim().length
