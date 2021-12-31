package day23_amphipod

import geom2d.Point
import geom2d.Rect
import geom2d.toAsciiArt
import util.printBoxed

/**
 * todo: add notes
 */
fun main() {
    util.solve(::partOne)
//    util.solve(::partTwo)
}

enum class Cell(var symbol: Char, val movementCost: Long = 0) {
    WALL('#'),
    OPEN('.'),
    AMBER('A', 1),
    BRONZE('B', 10),
    COPPER('C', 100),
    DESERT('D', 1000);

    fun isAmphipod() = movementCost != 0L
}

class Grid(val width: Int, val height: Int) {
    val bounds get() = Rect(width.toLong(), height.toLong())
    private val grid = Array(width * height) { Cell.WALL }

    private fun index(row: Int, col: Int) = row * width + col
    private fun coords(idx: Int) = Pair(idx / width, idx % width)

    operator fun get(row: Int, col: Int): Cell =
        grid[index(row, col)]

    operator fun get(p: Point) =
        get(p.y.toInt(), p.x.toInt())

    operator fun set(row: Int, col: Int, value: Cell) {
        grid[index(row, col)] = value
    }

    override fun toString() =
        bounds.toAsciiArt { (c, r) ->
            grid[index(r.toInt(), c.toInt())].symbol.toString()
        }

    fun canMove(p: Point): Boolean {
        val c = get(p)
        if (!c.isAmphipod()) return false
        val ns = p.orthogonalNeighbors()
            .filter { get(it) == Cell.OPEN }

    }
}

fun partOne(input: String): Long {
    val lines = input.lines()
    val width = lines.first().length
    val height = lines.size

    val grid = Grid(width, height)

    for ((row, line) in lines.withIndex()) {
        for ((col, c) in line.withIndex()) {
            when (c) {
                '.' -> grid[row, col] = Cell.OPEN
                'A' -> grid[row, col] = Cell.AMBER
                'B' -> grid[row, col] = Cell.BRONZE
                'C' -> grid[row, col] = Cell.COPPER
                'D' -> grid[row, col] = Cell.DESERT
                else -> {}
            }
        }
    }

    printBoxed(grid)

    grid.bounds
        .allPoints()
        .filter(grid::canMove)
        .forEach { println("$it => ${grid.get(it)}") }

    return -1
}

fun partTwo(input: String) = input.trim().length
