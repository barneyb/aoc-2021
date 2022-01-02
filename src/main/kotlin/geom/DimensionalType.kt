package geom

open class DimensionalType<E>(val dims: Array<E>) {

    val dimensions get() = dims.size

    operator fun get(index: Int) =
        dims[index]

    val x get() = dims[0]
    val y get() = dims[1]
    val z get() = dims[2]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DimensionalType<*>) return false

        if (!dims.contentEquals(other.dims)) return false

        return true
    }

    override fun hashCode(): Int {
        return dims.contentHashCode()
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(${dims.joinToString(", ")})"
    }

}
