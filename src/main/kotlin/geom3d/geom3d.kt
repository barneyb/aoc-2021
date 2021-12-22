package geom3d

import kotlin.math.abs

open class Point(val x: Long, val y: Long, val z: Long) {

    companion object {
        val ORIGIN = Point(0, 0, 0)
    }

    fun manhattanDistance() =
        manhattanDistance(ORIGIN)

    fun manhattanDistance(p: Point) =
        abs(p.x - x) + abs(p.y - y) + abs(p.z - z)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y, z=$z)"
    }

}
