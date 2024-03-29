package day09_smoke_basin

import geom2d.Point
import geom2d.Rect
import geom2d.asLinearOffset
import util.saveTextFile
import java.io.PrintWriter
import java.util.*

/**
 * "The 2D plane" needs a refresher. Move around it, find neighbors, a bit of
 * parsing, find the right data structures... Partially review, and also upping
 * the number of balls in the air a bit. Definitely into week two.
 *
 * Part two is the first graph traversal, where the nodes of the graph are the
 * locations and the edges are "neighbors". Part one's answer provides the start
 * points for traversing and the rules say `9` means stop. BFS or DFS will do
 * about the same, but visit marking is required. The last wrinkle is finding
 * the three largest basins - sounds like a heap-based priority queue!
 */
fun main() {
    util.solve(554, ::partOne) // 1774 is too high (>=, not >)
    util.solve(1017792, ::partTwo)
    saveTextFile(::csv, "csv")
}

private class Grid(input: String) {
    val width = input.indexOf('\n')
    val grid = input
        .filter { it != '\n' }
    val height = grid.length / width
    val bounds = Rect(width.toLong(), height.toLong())
    val lowPoints: Set<Point>

    init {
        lowPoints = mutableSetOf()
        bounds
            .allPoints()
            .forEach { p ->
                val n = grid[bounds.asLinearOffset(p).toInt()]
                if (p.orthogonalNeighbors(bounds)
                        .all {
                            n < grid[bounds.asLinearOffset(it).toInt()]
                        }
                ) {
                    lowPoints.add(p)
                }
            }
    }

    val basins: List<Set<Point>> by lazy {
        lowPoints.map { low ->
            val basin = mutableSetOf<Point>()
            val queue: Queue<Point> = LinkedList()
            queue.add(low)
            while (queue.isNotEmpty()) {
                val p = queue.remove()
                if (basin.contains(p)) continue
                if (grid[bounds.asLinearOffset(p).toInt()] == '9') continue
                basin.add(p)
                queue.addAll(p.orthogonalNeighbors(bounds))
            }
            basin
        }
    }

    operator fun get(p: Point) =
        grid[bounds.asLinearOffset(p).toInt()].digitToInt()
}

fun partOne(input: String) =
    Grid(input).let { grid ->
        grid.lowPoints.sumOf { p -> grid[p] + 1 }
    }

fun partTwo(input: String) =
    PriorityQueue<Int>(3)
        .also { pq ->
            Grid(input)
                .basins
                .forEach { b ->
                    pq.add(b.size)
                    if (pq.size > 3) pq.remove()
                }

        }
        .reduce(Int::times)

private fun csv(input: String, out: PrintWriter) {
    out.println("x,y,height,is_low_point,basin_number")
    Grid(input).apply {
        bounds.allPoints().forEach { p ->
            val height = this[p]
            out.print("${p.x},${p.y}")
            out.print(",$height,${if (lowPoints.contains(p)) 1 else 0}")
            if (height != 9)
                out.print(",${basins.indexOfFirst { it.contains(p) }}")
            out.println()
        }
    }
}
