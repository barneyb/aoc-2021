package geom

import kotlin.math.abs

class Vec(dims: Array<Long>) : DimensionalType<Long>(dims), Comparable<Vec> {

    companion object {
        fun origin(dimensions: Int) =
            Vec(Array(dimensions) { 0 })
    }

    constructor(x: Long) :
            this(arrayOf(x))

    constructor(x: Long, y: Long) :
            this(arrayOf(x, y))

    constructor(x: Long, y: Long, z: Long) :
            this(arrayOf(x, y, z))

    override fun compareTo(other: Vec): Int {
        for (i in 0 until dimensions) {
            val c = dims[i].compareTo(other.dims[i])
            if (c != 0) return c
        }
        return 0
    }

    fun manhattanDistance() =
        manhattanDistance(origin(dimensions))

    fun manhattanDistance(other: Vec): Long {
        var sum = 0L
        for (i in 0 until dimensions) {
            sum += abs(dims[i] - other.dims[i])
        }
        return sum
    }

    private fun combine(other: Vec, op: (Long, Long) -> Long): Vec {
        val ds = dims.copyOf()
        for (i in 0 until dimensions) {
            ds[i] = op(ds[i], other.dims[i])
        }
        return Vec(ds)
    }

    operator fun plus(other: Vec) =
        combine(other, Long::plus)

    operator fun minus(other: Vec) =
        combine(other, Long::minus)

    operator fun times(scalar: Long): Vec {
        val ds = dims.copyOf()
        for (i in 0 until dimensions) {
            ds[i] *= scalar
        }
        return Vec(ds)
    }

}
