package day15_chiton_risk

import geom2d.Point
import geom2d.Rect
import geom2d.asLinearOffset
import java.util.*

/**
 * 2D plane again. "You start... your destination" means a graph walk. "Lowest
 * total risk" means an optimization problem. The wrinkle is that the
 * optimization needs to be integrated into the walk, as the search space is
 * infinite, since there's no rules about returning to an already-visited
 * location. Such reentrance cannot yield an optimal route, of course, but we
 * can do better. As soon as we have _any_ total risk (minimal or not), any
 * partial routes which have already exceeded that threshold can be immediately
 * ignored.
 *
 * Part two's search space is _massive_ compared to part one. Generalizing the
 * pruning from part one to apply to each point (not just complete routes)
 * trades some memory for a lot of CPU, so well worth it. Using a priority queue
 * to always extend the lowest-risk routes as quickly as possible maximizes the
 * effectiveness of that pruning.
 *
 * The 5x5 map is also interesting. It's not a simple tiling, but each non-first
 * tile is still wholly determined by the first tile. So no need to actually
 * materialize the full map. If part one's map has a nice interface,
 * a decorator can be used to "project" it to five times its size (in each
 * direction), including the requisite increase in risk.
 */
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

data class Path(
    val at: Point,
    val totalRisk: Int,
    val visited: List<Point>?,
) : Comparable<Path> {

    constructor(at: Point, track: Boolean = false) : this(
        at,
        0,
        if (track) listOf(at) else null,
    )

    fun then(pos: Point, risk: Int) =
        Path(
            pos,
            totalRisk + risk,
            visited?.plus(pos)
        )

    override fun compareTo(other: Path) =
        totalRisk - other.totalRisk

}

fun partOne(input: String): Int =
    riskOfBestPath(SimpleGrid(input))

private fun riskOfBestPath(grid: Grid, sink: ((Path) -> Unit)? = null): Int {
    val queue: Queue<Path> = PriorityQueue()
    queue.add(Path(Point.ORIGIN, sink != null))
    val allRisks = HashMap<Point, Int>()
    val goal = grid.bottomRight
    while (queue.isNotEmpty()) {
        val p = queue.remove()
        if (p.at == goal) {
            sink?.invoke(p)
            return p.totalRisk
        }
        if (p.totalRisk >= allRisks.getOrDefault(p.at, Int.MAX_VALUE)) {
            continue
        }
        allRisks[p.at] = p.totalRisk
        sink?.invoke(p)
        p.at
            .orthogonalNeighbors(grid.bounds)
            .map { p.then(it, grid.getRiskAt(it)) }
            .forEach(queue::add)
    }
    throw IllegalStateException("found no path to the bottom corner?!")
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
