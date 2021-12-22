package day20_trench_map

import geom2d.Point
import geom2d.Rect
import geom2d.asLinearOffset
import geom2d.bounds

/**
 * Image processing! Just as described. Sharpening, blurring, edge detection, et
 * all are all based on this sort of algorithm. Take a kernel from the input, do
 * a thing to create an output pixel, repeat. Images are incredibly data-dense,
 * so making it work is only the tip of the iceberg. It has to be _fast_ or even
 * images of middling size will ruin your day.
 *
 * Part two illustrates the last. Nothing new, just "is it performant?"
 */
fun main() {
    util.solve(4917, ::partOne) // 5392, 4924 are too high; 4491 is too low
    util.solve(16389, ::partTwo)
}

private class Grid(input: String) {
    val width = input.indexOf('\n')
    val grid = input
        .filter { it != '\n' }
    val height = grid.length / width
    val bounds = Rect(width.toLong(), height.toLong())

    fun isLit(p: Point) =
        if (bounds.contains(p))
            grid[p.asLinearOffset(bounds).toInt()] == '#'
        else
            false
}

private fun Point.kernel() =
    listOf(
        // @formatter:off
        copy(x = x - 1, y = y - 1),
        copy(           y = y - 1),
        copy(x = x + 1, y = y - 1),
        copy(x = x - 1           ),
        this,
        copy(x = x + 1           ),
        copy(x = x - 1, y = y + 1),
        copy(           y = y + 1),
        copy(x = x + 1, y = y + 1),
        // @formatter:on
    )

fun partOne(input: String) = solve(input, 2)

private fun solve(input: String, steps: Int): Int {
    if (steps % 2 != 0) {
        throw IllegalArgumentException("odd numbers of steps mean infinite pixels!")
    }
    val idx = input.indexOf('\n')
    val mask = input.substring(0, idx)
    var curr = Grid(input.substring(idx + 2))
        .let { grid ->
            grid.bounds
                .allPoints()
                .filter(grid::isLit)
                .toSet()
        }
    var bounds = curr.bounds
    repeat(steps / 2) { halfTick ->
        bounds = bounds.expandedBy(3)
        repeat(2) {
            curr = bounds
                .allPoints()
                .filter { p ->
                    val maskIdx = p.kernel()
                        .map(curr::contains)
                        .fold(0) { n, b ->
                            (n shl 1) + (if (b) 1 else 0)
                        }
                    mask[maskIdx] == '#'
                }
                .toSet()
        }
        bounds = bounds.expandedBy(-1)
        curr = curr
            .filter(bounds::contains)
            .toSet()
    }
    return curr.size
}

fun partTwo(input: String) = solve(input, 50)
