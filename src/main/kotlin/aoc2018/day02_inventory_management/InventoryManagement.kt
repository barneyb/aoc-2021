package aoc2018.day02_inventory_management

import util.hasCharWithExactOccurrences

fun main() {
    util.solve(7776, ::partOne)
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
    val (doubles, triples) = input
        .lines()
        .fold(Pair(0, 0)) { (doubles, triples), l ->
            Pair(
                if (l.hasCharWithExactOccurrences(2)) doubles + 1 else doubles,
                if (l.hasCharWithExactOccurrences(3)) triples + 1 else triples
            )
        }
    return doubles * triples
}

fun partTwo(input: String) = input.trim().length
