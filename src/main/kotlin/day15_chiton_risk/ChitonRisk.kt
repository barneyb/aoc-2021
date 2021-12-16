package day15_chiton_risk

import geom2d.Point
import geom2d.Rect
import geom2d.asLinearOffset
import java.util.*

fun main() {
    util.solve(589, ::partOne)
    util.solve(::partTwo)
}

private class Grid(input: String) {
    val width = input.indexOf('\n')
    var grid = input
        .filter { it != '\n' }
        .map(Char::digitToInt)
    val height = grid.size / width
    val bounds = Rect(width.toLong(), height.toLong())
    val bottomRight get() = bounds.bottomRight

    fun getRiskAt(p: Point) =
        grid[p.asLinearOffset(bounds).toInt()]
}

data class Path(val at: Point, val totalRisk: Int) : Comparable<Path> {

    constructor(at: Point) : this(at, 0)

    fun then(pos: Point, risk: Int) =
        Path(
            pos,
            totalRisk + risk
        )

    override fun compareTo(other: Path) =
        totalRisk - other.totalRisk

}

fun partOne(input: String): Int =
    Grid(input).let { grid ->
        val queue: Queue<Path> = PriorityQueue()
        queue.add(Path(Point.ORIGIN))
        val allRisks = HashMap<Point, Int>()
        var minRisk = Int.MAX_VALUE
        val goal = grid.bottomRight
        while (queue.isNotEmpty()) {
            val p = queue.remove()
            val r = allRisks[p.at]
            if (p.totalRisk >= (r ?: minRisk)) continue
            allRisks[p.at] = p.totalRisk
            if (p.at == goal) {
                minRisk = minRisk.coerceAtMost(p.totalRisk)
                continue
            }
            p.at
                .orthogonalNeighbors(grid.bounds)
                .map { p.then(it, grid.getRiskAt(it)) }
                .forEach(queue::add)
        }
        minRisk
    }

fun partTwo(input: String) = input.trim().length
