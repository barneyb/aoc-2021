package day23_amphipod

import geom2d.Point
import util.printBoxed
import java.util.*

/**
 * todo: add notes
 */
fun main() {
    util.solve(18300, ::partOne)
    util.solve(50190, ::partTwo)
}

fun partOne(input: String) =
    solve(input.lines())

private fun solve(lines: List<String>): Long {
    val startGrid = lines.toGrid()
    printBoxed(startGrid)

    val queue: Queue<Grid> = PriorityQueue()
    queue.add(startGrid)
    val visited = hashSetOf<Grid>()
    var minEnergy = Long.MAX_VALUE
    var resultGrid: Grid? = null
    var steps = 0

    while (queue.isNotEmpty()) {
        ++steps
        val grid = queue.remove()
        if (grid.totalEnergy >= minEnergy) continue
        if (!visited.add(grid)) continue
        if (grid.complete) {
            resultGrid = grid
            minEnergy = grid.totalEnergy
//            printBoxed(
//                "$minEnergy @ ${grid.moveCount} moves",
//                TextColors.brightGreen
//            )
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
            for (x in (start.x - 1) downTo Grid.HALL_XS.first) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    enqueue(start, p)
                }
            }
            for (x in (start.x + 1)..Grid.HALL_XS.last) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    enqueue(start, p)
                }
            }
        }

        fun findDestInRoom(start: Point, breed: Cell): Point? {
            // ensure nothing's blocking the hallway
            val xs = if (start.x < breed.homeX)
                (start.x + 1)..breed.homeX
            else
                (start.x - 1) downTo breed.homeX
            for (x in xs) {
                if (!grid.isOpen(Point(x, Grid.HALL_Y))) return null
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
                val dest = findDestInRoom(start, breed)
                if (dest != null) {
//                    printBoxed(grid)
//                    println("$breed from $start to $dest")
                    enqueue(start, dest)
                }
            } else if (start.x == breed.homeX) { // home
                if ((start.y until (grid.height - 1)).any { y ->
                        grid[Point(start.x, y)].let {
                            it.isAmphipod() && it != breed
                        }
                    }) {
                    // blocking another
//                    printBoxed(grid)
//                    println("$breed into hall $start (blocking)")
                    moveToHall(start, breed)
                }
            } else { // wrong room
                for (y in (start.y - 1) downTo (Grid.HALL_Y + 1)) {
                    if (!grid.isOpen(Point(start.x, y))) {
                        // blocked...
                        return@forEach
                    }
                }
                val dest = findDestInRoom(start, breed)
                if (dest != null) {
//                    printBoxed(grid)
//                    println("direct $start -> $dest")
                    val mid = Point(breed.homeX, Grid.HALL_Y)
                    val next = grid.move(start, mid).move(mid, dest)
                    next.prev = grid // cull the midpoint move
                    queue.add(next)
                } else {
//                    printBoxed(grid)
//                    println("$breed into hall $start")
                    moveToHall(start, breed)
                }
            }
        }
    }

    if (minEnergy == Long.MAX_VALUE) {
        throw IllegalStateException("failed to find a solution ($steps steps)?!")
    }

    println("in $steps steps: $minEnergy")
    val stack: Deque<Grid> = ArrayDeque()
    var curr = resultGrid
    while (curr != null) {
        stack.push(curr)
        curr = curr.prev
    }
    stack.forEach {
        printBoxed(it)
    }
    return minEnergy
}

fun partTwo(input: String): Long {
    val lines = input.lines().toMutableList()
    lines.add(3, "  #D#C#B#A#  ")
    lines.add(4, "  #D#B#A#C#  ")
    return solve(lines)
}
