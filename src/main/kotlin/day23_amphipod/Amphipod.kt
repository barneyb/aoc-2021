package day23_amphipod

import com.github.ajalt.mordant.rendering.TextColors
import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import util.printBoxed
import java.util.*
import kotlin.math.abs

/**
 * todo: add notes
 */
fun main() {
    util.solve(18300, ::partOne)
//    util.solve(::partTwo)
}

enum class Cell(
    var symbol: Char,
    val stepEnergy: Long = 0,
    val homeX: Long = -1
) {
    WALL('#'),
    OPEN('.'),
    AMBER('A', 1, 3),
    BRONZE('B', 10, 5),
    COPPER('C', 100, 7),
    DESERT('D', 1000, 9);

    fun isAmphipod() = stepEnergy != 0L
}

private const val hallY = 1L
private const val width = 13
private const val height = 5
private val bounds = Rect(width.toLong(), height.toLong())

data class Grid(val totalEnergy: Long = 0, val moveCount: Int = 0) :
    Comparable<Grid> {
    private var grid = Array(width * height) { Cell.WALL }

    private fun index(row: Int, col: Int) = row * width + col
    private fun coords(idx: Int) = Pair(idx / width, idx % width)

    operator fun get(row: Int, col: Int): Cell =
        grid[index(row, col)]

    operator fun get(p: Point) =
        get(p.y.toInt(), p.x.toInt())

    operator fun set(row: Int, col: Int, value: Cell) {
        grid[index(row, col)] = value
    }

    operator fun set(p: Point, value: Cell) =
        set(p.y.toInt(), p.x.toInt(), value)

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
        return bounds.toAsciiArt { (c, r) ->
            if (r == 0L)
                if (digits.isEmpty()) "#" else digits.pop()
            else
                grid[index(r.toInt(), c.toInt())].symbol.toString()
        }
    }

    fun isHall(p: Point) = p.y == 1L

    fun isRoom(p: Point) = p.y > 1L

    fun isOpen(p: Point) =
        get(p) == Cell.OPEN

    fun canStopAt(p: Point, b: Cell) =
        if (p.y == 1L) // anywhere in the hall except above a room
            p.x !in 3..9L step 2
        else // the breed's home room
            p.x == b.homeX

    val amphipods: Map<Point, Cell>
        get() {
            val result = mutableMapOf<Point, Cell>()
            (1..11).forEach { c ->
                val cell = get(1, c)
                if (cell.isAmphipod()) result[Point(c.toLong(), 1)] = cell
            }
            (2..3).forEach { r ->
                (3..9).forEach { c ->
                    val cell = get(r, c)
                    if (cell.isAmphipod())
                        result[Point(c.toLong(), r.toLong())] = cell
                }
            }
            return result
        }

    val complete: Boolean
        get() = get(2, 3) == Cell.AMBER &&
                get(3, 3) == Cell.AMBER &&
                get(2, 5) == Cell.BRONZE &&
                get(3, 5) == Cell.BRONZE &&
                get(2, 7) == Cell.COPPER &&
                get(3, 7) == Cell.COPPER &&
                get(2, 9) == Cell.DESERT &&
                get(3, 9) == Cell.DESERT

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

    val minEnergyToFinish: Long by lazy {
        (1..11).sumOf { c ->
            val cell = this[hallY.toInt(), c]
            if (cell == Cell.OPEN) return@sumOf 0
            (abs(c - cell.homeX) + 1) * cell.stepEnergy
        } + (3..9 step 2).sumOf { c ->
            val cell = this[hallY.toInt() + 1, c]
            if (cell == Cell.OPEN) return@sumOf 0
            (abs(c - cell.homeX) + 2) * cell.stepEnergy
        } + (3..9 step 2).sumOf { c ->
            val cell = this[hallY.toInt() + 2, c]
            if (cell == Cell.OPEN) return@sumOf 0
            (abs(c - cell.homeX) + 3) * cell.stepEnergy
        }
    }

    override fun compareTo(other: Grid): Int =
        other.moveCount - moveCount
//        ((totalEnergy + minEnergyToFinish) - (other.totalEnergy + other.minEnergyToFinish)).toInt()

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

fun partOne(input: String): Long {
    val lines = input.lines()
    if (width != lines.first().length) throw IllegalStateException()
    if (height != lines.size) throw IllegalStateException()

    val startGrid = Grid()

    for ((row, line) in lines.withIndex()) {
        for ((col, c) in line.withIndex()) {
            when (c) {
                '.' -> startGrid[row, col] = Cell.OPEN
                'A' -> startGrid[row, col] = Cell.AMBER
                'B' -> startGrid[row, col] = Cell.BRONZE
                'C' -> startGrid[row, col] = Cell.COPPER
                'D' -> startGrid[row, col] = Cell.DESERT
                else -> {}
            }
        }
    }
    printBoxed(startGrid)

    val queue: Queue<Grid> = PriorityQueue()
    queue.add(startGrid)
    val visited = hashSetOf<Grid>()
    var minEnergy = Long.MAX_VALUE
    var steps = 0

    while (queue.isNotEmpty()) {
        ++steps
        val grid = queue.remove()
        if (grid.totalEnergy >= minEnergy) continue
        if (!visited.add(grid)) continue
        if (grid.complete) {
            minEnergy = grid.totalEnergy
            printBoxed(
                "$minEnergy @ ${grid.moveCount} moves",
                TextColors.brightGreen
            )
            continue
        }

        if (steps % 1_000_000 == 0) {
            printBoxed(grid)
            println("${queue.size} to go...")
        }

        fun enqueue(src: Point, dest: Point) {
            val next = grid.move(src, dest)
            queue.add(next)
        }

        fun moveToHall(
            start: Point,
            breed: Cell
        ) {
            for (x in start.x downTo 1) {
                val p = Point(x, hallY)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p, breed)) {
                    enqueue(start, p)
                }
            }
            for (x in start.x..11) {
                val p = Point(x, hallY)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p, breed)) {
                    enqueue(start, p)
                }
            }
        }

        grid.amphipods.forEach { (start, breed) ->
            if (grid.isHall(start)) {
                // move into room, if possible
                if (start.x < breed.homeX) {
                    for (x in (start.x + 1)..breed.homeX) {
                        if (!grid.isOpen(Point(x, start.y))) return@forEach
                    }
                } else {
                    for (x in (start.x - 1) downTo breed.homeX) {
                        if (!grid.isOpen(Point(x, start.y))) return@forEach
                    }
                }
                val pa = Point(breed.homeX, hallY + 1)
                val a = grid[pa]
                if (a != Cell.OPEN) return@forEach
                val pb = Point(breed.homeX, hallY + 2)
                val b = grid[pb]
                if (b != Cell.OPEN && b != breed) return@forEach
                val dest = if (b == Cell.OPEN) pb else pa
//                printBoxed(grid)
//                println("$breed from $start to $dest")
                enqueue(start, dest)
            } else if (start.x == breed.homeX) { // home
                if (start.y == 2L && grid[start.copy(y = start.y + 1)] != breed) {
//                    printBoxed(grid)
//                    println("$breed into hall $start (blocking)")
                    // blocking another
                    moveToHall(start, breed)
                }
            } else { // wrong room
                if (start.y == 2L || grid.isOpen(Point(start.x, 2L))) {
//                    printBoxed(grid)
//                    println("$breed into hall $start")
                    moveToHall(start, breed)
                }
            }
        }
    }

    println("in $steps steps: $minEnergy")
    return minEnergy
}

fun partTwo(input: String) = input.trim().length
