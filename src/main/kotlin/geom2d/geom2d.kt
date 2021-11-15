package geom2d

import kotlin.math.abs

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
}

enum class Turn {
    LEFT, RIGHT, AROUND
}

enum class Dir {
    NORTH, EAST, SOUTH, WEST;

    fun turn(turn: Turn) = when (turn) {
        Turn.LEFT -> when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }
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
    }

}

data class Step(val dir: Dir, val n: Long)

data class Heading(val location: Point, val dir: Dir) {

    fun turn(turn: Turn) =
        copy(dir = dir.turn(turn))

    fun step(steps: Long = 1) =
        copy(location = location.step(dir, steps))
}
