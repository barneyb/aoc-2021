package aoc2019.day22_slam_shuffle

import util.isAocAll
import java.util.*

fun main() {
    util.solve(3036, ::partOne)
    if (!isAocAll())
        util.solve(2019, ::partOneReverse)
    util.solve(70618172909245, ::partTwoReverse)
}

internal fun String.words() =
    this.split(" ")

internal fun List<Op>.forward(card: Long) =
    this.fold(card) { c, op ->
        op.forward(c)
    }

internal fun List<Op>.reverse(card: Long) =
    this.asReversed().fold(card) { c, op ->
        op.reverse(c)
    }

const val DECK_SIZE_ONE: Long = 10007
const val ITERATIONS_ONE: Long = 1

fun partOne(input: String) =
    partOne(input, DECK_SIZE_ONE, 2019, ITERATIONS_ONE)

fun partOne(
    input: String,
    deckSize: Long,
    card: Long,
    iterations: Long
): Long {
    var itrsLeft = iterations
    var c = card
    val d = object : TreeMap<Long, List<Op>>() {
        override operator fun get(key: Long): List<Op> {
            return super.get(key)!!
        }
    }
    d[1] = reduceOps(input.toOps(deckSize))
    var prev = 1L
    val cutoff = iterations / 2
    while (prev < cutoff) {
        val n = prev * 2
        d[n] = reduceOps(d[prev] + d[prev])
        prev = n
    }

    while (itrsLeft > 0) {
        val (n, ops) = d.lowerEntry(itrsLeft + 1)
        c = ops.forward(c)
        itrsLeft -= n
    }
    return c
}

fun partOneReverse(input: String) = input
    .toOps(DECK_SIZE_ONE)
    .reverse(3036)

const val DECK_SIZE_TWO: Long = 119_315_717_514_047
const val ITERATIONS_TWO: Long = 101_741_582_076_661

fun partTwo(input: String) =
    partTwo(
        input,
        DECK_SIZE_TWO,
        2020,
        ITERATIONS_TWO
    )

internal fun partTwo(
    input: String,
    deckSize: Long,
    card: Long,
    iterations: Long
): Long {
    val ops = input.toOps(deckSize)
    var c = card
    for (i in 0 until iterations) {
        c = ops.reverse(c)
    }
    return c
}

fun partTwoReverse(input: String) =
    partOne(
        input,
        DECK_SIZE_TWO,
        2020,
        DECK_SIZE_TWO - ITERATIONS_TWO - 1
    )
