package day11_dumbo_octopus

import java.util.*

fun main() {
    util.solve(1588, ::partOne)
    util.solve(517, ::partTwo)
}

fun partOne(input: String) =
    partOne(input, 100)

private class Grid(input: String) {
    val width = input.indexOf('\n')

    fun coords(idx: Int) =
        Pair(idx / width, idx % width)

    fun index(row: Int, col: Int) =
        row * width + col

    var grid = input
        .filter { it != '\n' }
        .map(Char::digitToInt)
    val height = grid.size / width

    var flashes = 0
    var ticks = 0

    fun tick(): Int {
        // everyone goes up by one
        val next = grid
            .map { it + 1 } as MutableList
        // greater than nine flashes
        val queue: Queue<Int> = LinkedList()
        fun illuminate(idx: Int) {
            queue.add(idx)
            next[idx] += 1
        }
        next.forEachIndexed { i, it ->
            if (it > 9) {
                queue.add(i)
            }
        }
        val flashed = mutableSetOf<Int>()
        while (queue.isNotEmpty()) {
            val i = queue.remove()
            if (next[i] <= 9) continue
            if (!flashed.add(i)) continue
            val (row, col) = coords(i)

            if (row > 0)
                illuminate(index(row - 1, col)) // north
            if (col < width - 1) {
                if (row > 0)
                    illuminate(index(row - 1, col + 1)) // north-east
                illuminate(index(row, col + 1)) // east
                if (row < height - 1)
                    illuminate(index(row + 1, col + 1)) // south-east
            }
            if (row < height - 1) {
                if (col > 0)
                    illuminate(index(row + 1, col - 1)) // south-west
                illuminate(index(row + 1, col)) // south
            }
            if (col > 0) {
                illuminate(index(row, col - 1)) // west
                if (row > 0)
                    illuminate(index(row - 1, col - 1)) // north-west
            }
        }
        // reset flashers to zero
        flashed.forEach {
            next[it] = 0
        }

        grid = next
        flashes += flashed.size
        ticks += 1

        return flashed.size
    }
}

fun partOne(input: String, steps: Int): Int {
    val grid = Grid(input)
    repeat(steps) { grid.tick() }
    return grid.flashes
}

fun partTwo(input: String): Int {
    val grid = Grid(input)
    val all = grid.width * grid.height
    while (true) {
        if (grid.tick() == all) {
            return grid.ticks
        }
    }
}