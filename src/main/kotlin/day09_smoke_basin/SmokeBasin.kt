package day09_smoke_basin

import geom2d.Dir
import geom2d.Point
import util.saveTextFile
import java.io.PrintWriter
import java.util.*

fun main() {
    util.solve(554, ::partOne) // 1774 is too high
    util.solve(1017792, ::partTwo)
    saveTextFile(::csv, "csv")
}

private class Grid(input: String) {
    val width: Int
    val grid: String
    val height: Int
    val lowPoints: Set<Point>

    init {
        width = input.indexOf('\n')
        grid = input
            .filter { it != '\n' }
        height = grid.length / width
        lowPoints = mutableSetOf()
        for (r in 0 until height) {
            for (c in 0 until width) {
                val n = grid[r * width + c]
                if (r > 0 && n >= grid[(r - 1) * width + c]) continue
                if (r < height - 1 && n >= grid[(r + 1) * width + c]) continue
                if (c > 0 && n >= grid[r * width + c - 1]) continue
                if (c < width - 1 && n >= grid[r * width + c + 1]) continue
                lowPoints.add(Point(c.toLong(), r.toLong()))
            }
        }
    }

    val basins: List<Set<Point>> by lazy {
        lowPoints.map { low ->
            val basin = mutableSetOf<Point>()
            val queue = LinkedList<Point>()
            queue.add(low)
            while (queue.isNotEmpty()) {
                val p = queue.remove()
                if (basin.contains(p)) continue
                if (get(p) == 9) continue
                basin.add(p)
                if (p.y > 0) queue.add(p.step(Dir.NORTH))
                if (p.y < height - 1) queue.add(p.step(Dir.SOUTH))
                if (p.x > 0) queue.add(p.step(Dir.WEST))
                if (p.x < width - 1) queue.add(p.step(Dir.EAST))
            }
            basin
        }
    }

    operator fun get(p: Point) =
        grid[(p.y * width + p.x).toInt()].digitToInt()
}

fun partOne(input: String) =
    Grid(input).let { grid ->
        grid.lowPoints.sumOf { p -> grid[p] + 1 }
    }

fun partTwo(input: String) =
    Grid(input)
        .basins
        .map { it.size }
        .sorted()       // should use a priority queue
        .asReversed()   // here, but I don't have one
        .take(3)        // readily available...
        .reduce(Int::times)

private fun csv(input: String, out: PrintWriter) {
    out.println("x,y,height,is_low_point,basin_number")
    Grid(input).apply {
        for (y in 0L until height) {
            for (x in 0L until width) {
                out.print("$x,$y")
                val p = Point(x, y)
                val height = this[p]
                out.print(",$height,${if (lowPoints.contains(p)) 1 else 0}")
                if (height != 9)
                    out.print(",${basins.indexOfFirst { it.contains(p) }}")
                out.println()
            }
        }
    }
}
