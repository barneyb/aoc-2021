package aoc2016.day02_bathroom_security

import geom2d.Dir.*
import geom2d.toDir

fun main() {
    util.solve("61529", ::partOne)
    util.solve("C2C28", ::partTwo)
}

internal fun String.asKeymap(): Array<IntArray> {
    val map = mutableMapOf<Int, IntArray>()
    var max = -1
    val grid = trimIndent().lines().map {
        it.map {
            when (it) {
                ' ' -> -1
                else -> it.digitToInt(16)
            }
        }
    }
    grid.forEachIndexed { row, nums ->
        nums.forEachIndexed inner@{ col, n ->
            if (n < 0) return@inner
            max = max.coerceAtLeast(n)
            val arr = intArrayOf(n, n, n, n)
            map[n] = arr
            // up
            if (row > 0) {
                val prevRow = grid[row - 1]
                if (prevRow.size > col && prevRow[col] >= 0) {
                    arr[NORTH.ordinal] = prevRow[col]
                }
            }
            // right
            val r = nums.subList(col + 1, nums.size).find { it > 0 }
            if (r != null) {
                arr[EAST.ordinal] = r
            }
            // down
            if (row < grid.size - 1) {
                val nextRow = grid[row + 1]
                if (nextRow.size > col && nextRow[col] >= 0) {
                    arr[SOUTH.ordinal] = nextRow[col]
                }
            }
            // left
            val l = nums.subList(0, col).asReversed().find { it > 0 }
            if (l != null) {
                arr[WEST.ordinal] = l
            }
        }
    }
    return Array(max + 1) {
        map.getOrDefault(it, intArrayOf())
    }
}

private fun solve(input: String, keymap: Array<IntArray>) = input
    .lines()
    .asSequence()
    .map { it.map(Char::toDir) }
    .runningFold(5) { btn, steps ->
        steps.fold(btn) { b, s ->
            keymap[b][s.ordinal]
        }
    }
    .drop(1) // don't want the starting five...
    .joinToString(separator = "") {
        it.toString(16).uppercase()
    }

fun partOne(input: String) =
    solve(
        input, """
        1 2 3
        4 5 6
        7 8 9
        """.asKeymap()
    )

fun partTwo(input: String) =
    solve(
        input, """
            1
          2 3 4
        5 6 7 8 9
          A B C
            D
        """.asKeymap()
    )
