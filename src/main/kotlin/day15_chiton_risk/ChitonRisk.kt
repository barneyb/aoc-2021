package day15_chiton_risk

import geom2d.Point
import geom2d.Rect
import geom2d.asLinearOffset
import java.util.*

fun main() {
    util.solve(589, ::partOne)
    util.solve(2885, ::partTwo)
}

private abstract class Grid {
    abstract val width: Int
    abstract val height: Int
    val bounds by lazy {
        Rect(width.toLong(), height.toLong())
    }
    val bottomRight: Point get() = bounds.bottomRight

    abstract fun getRiskAt(p: Point): Int
}

private class SimpleGrid(input: String) : Grid() {
    override val width = input.indexOf('\n')
    var grid = input
        .filter { it != '\n' }
        .map(Char::digitToInt)
    override val height = grid.size / width

    override fun getRiskAt(p: Point) =
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
    riskOfBestPath(SimpleGrid(input))

private fun riskOfBestPath(grid: Grid): Int {
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
    return minRisk
}

private class TiledGrid(
    val base: Grid,
    n: Int,
    override val width: Int = base.width * n,
    override val height: Int = base.height * n
) : Grid() {

    override fun getRiskAt(p: Point): Int {
        val raw = base.getRiskAt(
            Point(
                p.x % base.width,
                p.y % base.height
            )
        )
        return ((raw + p.x / base.width + p.y / base.height - 1) % 9 + 1).toInt()
    }
}

fun partTwo(input: String) =
    riskOfBestPath(TiledGrid(SimpleGrid(input), 5))
