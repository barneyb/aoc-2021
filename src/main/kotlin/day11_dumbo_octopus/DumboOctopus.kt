package day11_dumbo_octopus

import geom2d.Rect
import geom2d.asLinearOffset
import geom2d.asPoint
import java.util.*

/**
 * Conway's game of life, but with cascade! Since each state is wholly derived
 * from the prior state, that suggests an immutable fold of some sort. But with
 * the cascade, it's not going to be a _simple_ fold. Each iteration will need
 * to have some stateful recursive walk inside to cascade the flashes from
 * octopus to octopus.
 *
 * Part two's just a matter of setting up a forever loop and some way of knowing
 * how many flashes occurred on a given step. The former's simple; the latter's
 * already known - though perhaps not explicitly - so that the right octopuses
 * can be reset to zero after the cascade.
 */
fun main() {
    util.solve(1588, ::partOne)
    util.solve(517, ::partTwo)
}

fun partOne(input: String) =
    partOne(input, 100)

private class Grid(input: String) {
    val width = input.indexOf('\n')
    var grid = input
        .filter { it != '\n' }
        .map(Char::digitToInt)
    val height = grid.size / width
    val bounds = Rect(width.toLong(), height.toLong())

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
            bounds.asPoint(i.toLong())
                .neighbors(bounds)
                .forEach { illuminate(bounds.asLinearOffset(it).toInt()) }
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
    val all = grid.bounds.pointCount.toInt()
    while (true) {
        if (grid.tick() == all) {
            return grid.ticks
        }
    }
}
