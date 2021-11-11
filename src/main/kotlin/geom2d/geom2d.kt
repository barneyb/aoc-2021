package geom2d

import kotlin.math.abs

data class Point(val x: Long, val y: Long) {

    fun step(s: Step) =
        step(s.dir, s.n)

    fun step(dir: Dir, steps: Long = 1) =
        when (dir) {
            Dir.NORTH -> copy(y = y - steps)
            Dir.EAST -> copy(x = x + steps)
            Dir.SOUTH -> copy(y = y + steps)
            Dir.WEST -> copy(x = x - steps)
        }

    fun mannDist() =
        mannDist(Point(0, 0))

    fun mannDist(p: Point) =
        abs(p.x - x) + abs(p.y - y)
}

enum class Dir {
    NORTH, EAST, SOUTH, WEST
}

data class Step(val dir: Dir, val n: Long)
