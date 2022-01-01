package day23_amphipod

import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import java.util.*

data class Grid(
    val height: Int = 5,
    val totalEnergy: Long = 0,
    val moveCount: Int = 0
) :
    Comparable<Grid> {

    companion object {
        const val HALL_Y = 1L
        const val WIDTH = 13
        val HALL_XS get() = 1..(WIDTH - 2)
        val ROOM_XS get() = 3..9L step 2
    }

    private var grid = Array(WIDTH * height) { Cell.WALL }
    private val bounds = Rect(WIDTH.toLong(), height.toLong())

    private val roomYs get() = (height - 2) downTo (HALL_Y + 1)

    private fun index(x: Int, y: Int) = y * WIDTH + x
    private fun index(x: Long, y: Long) = y.toInt() * WIDTH + x.toInt()

    private operator fun get(x: Long, y: Long) =
        grid[index(x, y)]

    operator fun get(p: Point) =
        get(p.x, p.y)

    operator fun set(x: Int, y: Int, value: Cell) {
        grid[index(x, y)] = value
    }

    operator fun set(x: Long, y: Long, value: Cell) {
        grid[index(x, y)] = value
    }

    operator fun set(p: Point, value: Cell) =
        set(p.x, p.y, value)

    override fun toString(): String {
        var e = totalEnergy
        val digits: Stack<String> = Stack()
        digits.push(" ")
        do {
            digits.push((e % 10).toString())
            e /= 10
        } while (e > 0)
        digits.push(" ")
        digits.push("#")
        return bounds.toAsciiArt { (x, y) ->
            if (y == 0L)
                if (digits.isEmpty()) "#" else digits.pop()
            else
                get(x, y).symbol.toString()
        }
    }

    fun isHall(p: Point) = p.y == HALL_Y

    fun isRoom(p: Point) = p.y > HALL_Y

    fun isOpen(p: Point) =
        get(p) == Cell.OPEN

    fun canStopAt(p: Point): Boolean {
        if (p.y != HALL_Y) throw IllegalArgumentException("non-hall point $p?!")
        return p.x !in ROOM_XS
    }

    val amphipods: Map<Point, Cell>
        get() {
            val result = mutableMapOf<Point, Cell>()
            HALL_XS.forEach { c ->
                val p = Point(c.toLong(), HALL_Y)
                val cell = get(p)
                if (cell.isAmphipod()) {
                    result[p] = cell
                }
            }
            roomYs.forEach { y ->
                ROOM_XS.forEach { x ->
                    val p = Point(x, y)
                    val cell = get(p)
                    if (cell.isAmphipod()) {
                        result[p] = cell
                    }
                }
            }
            return result
        }

    val complete: Boolean
        get() = get(3, 2) == Cell.AMBER &&
                get(3, 3) == Cell.AMBER &&
                get(5, 2) == Cell.BRONZE &&
                get(5, 3) == Cell.BRONZE &&
                get(7, 2) == Cell.COPPER &&
                get(7, 3) == Cell.COPPER &&
                get(9, 2) == Cell.DESERT &&
                get(9, 3) == Cell.DESERT

    fun move(src: Point, dest: Point): Grid {
        if (get(dest) != Cell.OPEN)
            throw IllegalArgumentException("Can't move to $dest; it isn't open?!")
        val amphipod = get(src)
        if (!amphipod.isAmphipod())
            throw IllegalArgumentException("Can't move $amphipod from $src; it isn't an amphipod?!")
        val next = copy(
            totalEnergy = totalEnergy +
                    (amphipod.stepEnergy * src.manhattanDistance(dest)),
            moveCount = moveCount + 1,
        )
        next.grid = grid.copyOf()
        next[dest] = amphipod
        next[src] = Cell.OPEN
        return next
    }

    override fun compareTo(other: Grid): Int =
        other.moveCount - moveCount

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid

        if (totalEnergy != other.totalEnergy) return false
        if (moveCount != other.moveCount) return false
        if (!grid.contentEquals(other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalEnergy.hashCode()
        result = 31 * result + moveCount
        result = 31 * result + grid.contentHashCode()
        return result
    }

}
