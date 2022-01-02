package geom

import util.contains
import util.intersects
import util.size
import kotlin.math.max
import kotlin.math.min

@Suppress("EmptyRange")
val EMPTY_RANGE = 0..-1L

class Cuboid(dims: Array<LongRange>) : DimensionalType<LongRange>(dims) {

    constructor(x: LongRange) :
            this(arrayOf(x))

    constructor(x: LongRange, y: LongRange) :
            this(arrayOf(x, y))

    constructor(x: LongRange, y: LongRange, z: LongRange) :
            this(arrayOf(x, y, z))

    val size: Long
        get() {
            var product = 1L
            for (d in dims) {
                if (d.isEmpty()) return 0
                product *= d.size
            }
            return product
        }

    fun isEmpty(): Boolean {
        for (d in dims) {
            if (d.isEmpty()) return true
        }
        return false
    }

    val width get() = dims[0].size
    val height get() = dims[1].size
    val depth get() = dims[2].size

    fun intersects(other: Cuboid): Boolean {
        for (i in dims.indices) {
            if (!dims[i].intersects(other[i])) return false
        }
        return true
    }

    fun contains(other: Cuboid): Boolean {
        for (i in dims.indices) {
            if (!dims[i].contains(other[i])) return false
        }
        return true
    }

    fun intersection(other: Cuboid): Cuboid {
        val result = arrayOfNulls<LongRange>(dimensions)
        for (i in dims.indices) {
            val a = dims[i]
            val b = other[i]
            val r = max(a.first, b.first)..min(a.last, b.last)
            result[i] = if (r.isEmpty()) EMPTY_RANGE else r
        }
        @Suppress("UNCHECKED_CAST")
        return Cuboid(result as Array<LongRange>)
    }
}
