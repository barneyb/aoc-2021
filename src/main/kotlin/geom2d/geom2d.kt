package geom2d

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Rect(
    val x1: Long, val y1: Long, val x2: Long, val y2: Long
) {

    val width = x2 - x1 + 1
    val height = y2 - y1 + 1

    val topLeft get() = Point(x1, y1)
    val topRight get() = Point(x2, y1)
    val bottomLeft get() = Point(x1, y2)
    val bottomRight get() = Point(x2, y2)

    constructor(width: Long, height: Long) : this(Point.ORIGIN, width, height)

    constructor(p1: Point, p2: Point) : this(p1.x, p1.y, p2.x, p2.y)

    constructor(p: Point, width: Long, height: Long) : this(
        p.x,
        p.y,
        p.x + width - 1,
        p.y + height - 1
    )

    val xs get() = if (x1 <= x2) x1..x2 else x1 downTo x2
    val ys get() = if (y1 <= y2) y1..y2 else y1 downTo y2

    val pointCount get() = (abs(x2 - x1) + 1) * (abs(y2 - y1) + 1)

    /**
     * A sequence of all points in the Rect, in English reading order.
     */
    fun allPoints(): Sequence<Point> {
        var row = y1
        var col = x1 - 1
        return generateSequence {
            col += 1
            if (col > x2) {
                col = x1
                row += 1
            }
            if (row <= y2) {
                Point(col, row)
            } else null
        }
    }

    fun contains(p: Point) =
        p.x >= x1 && p.y >= y1 && p.x <= x2 && p.y <= y2

    fun expandedTo(p: Point) =
        if (contains(p)) this else Rect(
            min(x1, p.x),
            min(y1, p.y),
            max(x2, p.x),
            max(y2, p.y),
        )

    fun mirrorX() = Rect(x2, y1, x1, y2)
    fun mirrorY() = Rect(x1, y2, x2, y1)

    /**
     * Expands the Rect by the specified delta(s).
     */
    fun expandedBy(dx: Int, dy: Int = dx) =
        Rect(
            x1 - dx,
            y1 - dy,
            x2 + dx,
            y2 + dy
        )

}

/**
 * Draw the rect as an ASCII art grid, using the passed renderer to draw each
 * "pixel". While you can return any char sequence, you almost certainly want
 * to return single chars. At the very least, each `x` value should have the
 * same length for all `y` values.
 */
fun Rect.toAsciiArt(render: (Point) -> CharSequence) =
    ys.joinToString(separator = "\n") { y ->
        xs.joinToString(separator = "") { x ->
            render(Point(x, y))
        }
    }

fun Rect.asPoint(linearOffset: Long) =
    Point(linearOffset % width, linearOffset / width)

fun Rect.asLinearOffset(p: Point) =
    p.y * width + p.x

fun Collection<Point>.toStringGrid(
    on: CharSequence = "█",
    off: CharSequence = "."
) =
    bounds.toAsciiArt {
        if (contains(it)) on else off
    }

val Collection<Point>.bounds
    get() =
        drop(1).fold(Rect(first(), 1, 1)) { r, p ->
            Rect(
                min(r.x1, p.x),
                min(r.y1, p.y),
                max(r.x2, p.x),
                max(r.y2, p.y),
            )
        }

data class Point(val x: Long, val y: Long) {

    companion object {
        val ORIGIN = Point(0, 0)
    }

    fun step(dir: Dir, steps: Long = 1) =
        when (dir) {
            Dir.NORTH -> copy(y = y - steps)
            Dir.EAST -> copy(x = x + steps)
            Dir.SOUTH -> copy(y = y + steps)
            Dir.WEST -> copy(x = x - steps)
        }

    fun manhattanDistance() =
        manhattanDistance(ORIGIN)

    fun manhattanDistance(p: Point) =
        abs(p.x - x) + abs(p.y - y)

    fun neighbors() =
        listOf(
            // @formatter:off
            copy(           y = y - 1),
            copy(x = x + 1, y = y - 1),
            copy(x = x + 1           ),
            copy(x = x + 1, y = y + 1),
            copy(           y = y + 1),
            copy(x = x - 1, y = y + 1),
            copy(x = x - 1           ),
            copy(x = x - 1, y = y - 1),
            // @formatter:on
        )

    fun neighbors(bounds: Rect) =
        neighbors().filter(bounds::contains)

    fun diagonalNeighbors() =
        listOf(
            copy(x = x + 1, y = y - 1),
            copy(x = x + 1, y = y + 1),
            copy(x = x - 1, y = y + 1),
            copy(x = x - 1, y = y - 1),
        )

    fun diagonalNeighbors(bounds: Rect) =
        diagonalNeighbors().filter(bounds::contains)

    fun orthogonalNeighbors() =
        listOf(
            copy(y = y - 1),
            copy(x = x + 1),
            copy(y = y + 1),
            copy(x = x - 1),
        )

    fun orthogonalNeighbors(bounds: Rect) =
        orthogonalNeighbors().filter(bounds::contains)
}

fun Long.asPoint(bounds: Rect) =
    bounds.asPoint(this)

fun Point.asLinearOffset(bounds: Rect) =
    bounds.asLinearOffset(this)

fun Char.toDir() = when (this) {
    'N', '^', 'U' -> Dir.NORTH
    'E', '>', 'R' -> Dir.EAST
    'S', 'v', 'D' -> Dir.SOUTH
    'W', '<', 'L' -> Dir.WEST
    else -> throw IllegalArgumentException("Unknown '$this' direction")
}

/**
 * The four cardinal directions, starting at NORTH, and moving clockwise.
 */
enum class Dir {
    NORTH, EAST, SOUTH, WEST;
}
