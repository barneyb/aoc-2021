package geom

data class Vec(val values: List<Long>) {
    constructor(x: Long, y: Long) : this(listOf(x, y))
    constructor(x: Long, y: Long, z: Long) : this(listOf(x, y, z))
    constructor(vararg coords: Long) : this(coords.toList())

    val dimensions get() = values.size

    operator fun get(index: Int) = values[index]

    val x get() = values[0]
    val y get() = values[1]
    val z get() = values[2]
}
