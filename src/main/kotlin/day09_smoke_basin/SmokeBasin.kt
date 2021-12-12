package day09_smoke_basin

import geom2d.CharGrid
import geom2d.Point
import util.saveTextFile
import java.io.PrintWriter
import java.util.*

fun main() {
    util.solve(554, ::partOne) // 1774 is too high (<= not <, idiot)
    // 115.889μs initially
    // 563.956μs w/ extracted Grid type
    util.solve(1017792, ::partTwo)
    // 943.85μs initially
    // 1.410678ms w/ extracted Grid type
    saveTextFile(::csv, "csv")
}

private class Grid(input: String) : CharGrid<Int>(input, Char::digitToInt) {
    val lowPoints: Set<Point>

    init {
        lowPoints = mutableSetOf()
        allPoints().forEach { p ->
            val n = get(p)
            if (neighbors(p).all { get(it) > n })
                lowPoints.add(p)
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
                queue.addAll(neighbors(p))
            }
            basin
        }
    }
}

fun partOne(input: String) =
    Grid(input).let { grid ->
        grid.lowPoints.sumOf { p -> grid[p] + 1 }
    }

fun partTwo(input: String) =
    TreeSet<Int>() // poor-man's "heap"
        .also { pq ->
            Grid(input)
                .basins
                .map { it.size }
                .forEach {
                    pq.add(it)
                    if (pq.size > 3) pq.remove(pq.first())
                }
        }
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
