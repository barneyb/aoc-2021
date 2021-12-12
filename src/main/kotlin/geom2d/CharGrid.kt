package geom2d

/**
 * I represent a rectangular grid where each location has an associated T.
 * Conceptually a `Map<Point, T>`, but not really.
 */
open class CharGrid<T>(
    input: String,
    transform: (Char) -> T
) {
    val width: Int
    private val grid: List<T>
    val height: Int

    init {
        width = input.indexOf('\n')
        grid = input
            .filter { it != '\n' }
            .map(transform)
        height = grid.size / width
    }

    operator fun get(key: Point) =
        grid[(key.y * width + key.x).toInt()]

    /**
     * Iterator over all points in the grid, in "English reading order".
     */
    fun allPoints(): Sequence<Point> {
        var row = 0
        var col = -1
        return generateSequence {
            col += 1
            if (col == width) {
                col = 0
                row += 1
            }
            if (row < height) {
                Point(col.toLong(), row.toLong())
            } else null
        }
    }

    /**
     * Iterate over the four (usually) points orthogonally neighboring the given
     * point.
     */
    fun neighbors(p: Point): Sequence<Point> {
        var n = 0

        fun guard(accept: Int): Boolean {
            if (n == accept) {
                n += 1
                return true
            }
            return false
        }

        return generateSequence {
            if (guard(0) && p.y > 0)
                return@generateSequence p.step(Dir.NORTH)
            if (guard(1) && p.x < width - 1)
                return@generateSequence p.step(Dir.EAST)
            if (guard(2) && p.y < height - 1)
                return@generateSequence p.step(Dir.SOUTH)
            if (guard(3) && p.x > 0)
                return@generateSequence p.step(Dir.WEST)
            null
        }
    }

}
