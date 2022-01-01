package day23_amphipod

import com.github.ajalt.mordant.rendering.TextColors
import geom2d.Point
import util.printBoxed
import java.util.*

/**
 * todo: add notes
 */
fun main() {
    util.solve(18300, ::partOne)
//    util.solve(::partTwo)
}

fun partOne(input: String) =
    solve(input.lines())

private fun solve(lines: List<String>): Long {
    if (Grid.WIDTH != lines.first().length) throw IllegalStateException()

    val startGrid = Grid(height = lines.size)

    for ((y, line) in lines.withIndex()) {
        for ((x, c) in line.withIndex()) {
            when (c) {
                '.' -> startGrid.set(x, y, Cell.OPEN)
                'A' -> startGrid.set(x, y, Cell.AMBER)
                'B' -> startGrid.set(x, y, Cell.BRONZE)
                'C' -> startGrid.set(x, y, Cell.COPPER)
                'D' -> startGrid.set(x, y, Cell.DESERT)
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
            if (!visited.contains(next)) {
//                println("move $src -> $dest")
                queue.add(next)
            }
        }

        fun moveToHall(start: Point, breed: Cell) {
            for (x in start.x downTo Grid.HALL_XS.first) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    enqueue(start, p)
                }
            }
            for (x in start.x..Grid.HALL_XS.last) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    enqueue(start, p)
                }
            }
        }

        fun destInRoom(start: Point, breed: Cell): Point? {
            // ensure nothing's blocking the hallway
            val xs = if (start.x < breed.homeX)
                (start.x + 1)..breed.homeX
            else
                breed.homeX until start.x
            for (x in xs) {
                if (!grid.isOpen(Point(x, start.y))) return null
            }
            // find the lowest non-blocking slot in the room
            for (y in (grid.height - 2) downTo (Grid.HALL_Y + 1)) {
                val dest = Point(breed.homeX, y)
                val c = grid[dest]
                if (c == Cell.OPEN) {
                    // GO GO GO
                    return dest
                }
                if (c != breed) {
                    // still gotta clear that guy out
                    break
                }
            }
            return null
        }

        grid.amphipods.forEach { (start, breed) ->
            if (grid.isHall(start)) {
                val dest = destInRoom(start, breed)
                if (dest != null) {
//                    printBoxed(grid)
//                    println("$breed from $start to $dest")
                    enqueue(start, dest)
                }
            } else if (start.x == breed.homeX) { // home
                if (start.y == 2L && grid[start.copy(y = start.y + 1)] != breed) {
//                    printBoxed(grid)
//                    println("$breed into hall $start (blocking)")
                    // blocking another
                    moveToHall(start, breed)
                }
            } else { // wrong room
                val dest = destInRoom(start, breed)
                if (dest != null) {
                    println("move $start directly to $dest")
                    val mid = Point(breed.homeX, Grid.HALL_Y)
                    queue.add(grid.move(start, mid).move(mid, dest))
                } else {
                    if (start.y == 2L || grid.isOpen(Point(start.x, 2L))) {
//                        printBoxed(grid)
//                        println("$breed into hall $start")
                        moveToHall(start, breed)
                    }
                }
            }
        }
    }

    println("in $steps steps: $minEnergy")
    return minEnergy
}

fun partTwo(input: String) = input.trim().length
