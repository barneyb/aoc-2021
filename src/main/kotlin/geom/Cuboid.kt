package geom

import kotlin.math.max
import kotlin.math.min

fun LongRange.intersects(b: LongRange) =
    b.first in first..last || b.last in first..last

fun LongRange.contains(b: LongRange) =
    b.first in first..last && b.last in first..last

val LongRange.size
    get() =
        last - first + 1

@Suppress("EmptyRange")
val EMPTY_RANGE = 0L..-1

data class Cuboid(val dims: List<LongRange>) {

    constructor(x: LongRange, y: LongRange) :
            this(listOf(x, y))

    constructor(x: LongRange, y: LongRange, z: LongRange) :
            this(listOf(x, y, z))

    constructor(vararg dims: LongRange) :
            this(dims.toList())

    val dimensions get() = dims.size

    operator fun get(index: Int) = dims[index]

    val size
        get() =
            dims
                .map { max(0, it.size) }
                .fold(1, Long::times)

    fun isEmpty() =
        dims.any { it.last < it.first }

    val x get() = dims[0]
    val y get() = dims[1]
    val z get() = dims[2]

    val width get() = dims[0].size
    val height get() = dims[1].size
    val depth get() = dims[2].size

    fun intersects(other: Cuboid) =
        dims
            .zip(other.dims)
            .all { (a, b) ->
                a.intersects(b)
            }

    fun contains(other: Cuboid): Boolean {
        for (i in dims.indices) {
            if (!dims[i].contains(other[i])) return false
        }
        return true
    }

    fun intersection(other: Cuboid) =
        Cuboid(
            dims
                .zip(other.dims)
                .map { (a, b) ->
                    max(a.first, b.first)..min(a.last, b.last)
                }
                .map {
                    if (it.last < it.first) EMPTY_RANGE else it
                }
        )

}
