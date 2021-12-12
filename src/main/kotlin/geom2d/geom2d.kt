package geom2d

import kotlin.math.abs

data class Rect(
    val x1: Long, val y1: Long, val x2: Long, val y2: Long
) {

    val width = x2 - x1 + 1
    val height = y2 - y1 + 1

    constructor(width: Long, height: Long) : this(Point.ORIGIN, width, height)

    constructor(p: Point, width: Long, height: Long) : this(
        p.x,
        p.y,
        p.x + width - 1,
        p.y + height - 1
    )

    fun allPoints(): Sequence<Point> {
        var row = 0L
        var col = -1L
        return generateSequence {
            col += 1
            if (col == width) {
                col = 0
                row += 1
            }
            if (row < height) {
                Point(col, row)
            } else null
        }
    }

    fun contains(p: Point) =
        p.x >= x1 && p.y >= y1 && p.x <= x2 && p.y <= y2

    fun asPoint(linearOffset: Long) =
        Point(linearOffset % width, linearOffset / width)

    fun asLinearOffset(p: Point) =
        p.y * width + p.x
}

data class Point(val x: Long, val y: Long) {

    companion object {
        val ORIGIN = Point(0, 0)
    }

    fun step(s: Step) =
        step(s.dir, s.n)

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
            copy(/**/       y = y - 1),
            copy(x = x + 1, y = y - 1),
            copy(x = x + 1       /**/),
            copy(x = x + 1, y = y + 1),
            copy(/**/       y = y + 1),
            copy(x = x - 1, y = y + 1),
            copy(x = x - 1       /**/),
            copy(x = x - 1, y = y - 1),
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

fun Char.toTurn() = when (this) {
    'S' -> Turn.STRAIGHT
    'R' -> Turn.RIGHT
    'A' -> Turn.AROUND
    'L' -> Turn.LEFT
    else -> throw IllegalArgumentException("Unknown '$this' turn")
}

/**
 * The four 90-degree turns, starting with STRAIGHT, and moving clockwise
 */
enum class Turn {
    STRAIGHT, RIGHT, AROUND, LEFT
}

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

    fun turn(turn: Turn) = when (turn) {
        Turn.STRAIGHT -> this
        Turn.RIGHT -> when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
        Turn.AROUND -> when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }
        Turn.LEFT -> when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
    }

}

data class Step(val dir: Dir, val n: Long)

data class Heading(val location: Point, val dir: Dir) {

    companion object {
        val ORIGIN = Heading(Point.ORIGIN, Dir.NORTH)
    }

    fun turn(turn: Turn) =
        copy(dir = dir.turn(turn))

    fun step(steps: Long = 1) =
        copy(location = location.step(dir, steps))

    fun manhattanDistance() =
        location.manhattanDistance()

    fun manhattanDistance(p: Point) =
        location.manhattanDistance(p)

}
