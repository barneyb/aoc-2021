package aoc2018.day02_inventory_management

import util.hasCharWithExactOccurrences

fun main() {
    util.solve(7776, ::partOne)
    util.solve("wlkigsqyfecjqqmnxaktdrhbz", ::partTwo)
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

fun partTwo(input: String): String {
    val lines = input.lines()
    lines.forEachIndexed { currIdx, src ->
        lines.subList(0, currIdx).forEach { tgt ->
            var idx = -1
            for (i in 0 until tgt.length) {
                if (src[i] != tgt[i]) {
                    if (idx < 0) {
                        idx = i
                    } else {
                        return@forEach // continue
                    }
                }
            }
            // woo!
            return src.substring(0, idx) + src.substring(idx + 1)
        }
    }
    throw IllegalStateException("huh?")
}
