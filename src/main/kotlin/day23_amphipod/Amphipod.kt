package day23_amphipod

import geom2d.Point
import util.printBoxed
import java.util.*

/**
 * Another explore-the-map sort of puzzle, but with many (well, eight) walkers
 * and a bunch of arcane rules for how they can walk. This means the walk will
 * be HUGE, as in most states there are four different amphipods which can take
 * the "next" step and that step may have many possibilities. The maximum number
 * of steps required to "win" is only sixteen, but the branching structure is
 * massive. Prioritizing the search by energy (like Chitons #15) is nearly
 * irrelevant; what matters is avoiding re-walking already walked paths and
 * avoiding creating of spurious interim paths.
 *
 * Part two is more of the same. The search space is larger, as is the frequency
 * of already-walked paths. And, of course, if you hard-coded the shape of the
 * map to solve part one - a fairly reasonable choice at that point - you get a
 * nice slap on the wrist with a ruler.
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

    val queue: Queue<Grid> = ArrayDeque()
    queue.add(startGrid)
    val visited = hashSetOf<Grid>()
    var minEnergy = Long.MAX_VALUE
    var resultGrid: Grid? = null
    var steps = 0
    var energySkips = 0
    var dupeSkips = 0

    while (queue.isNotEmpty()) {
        ++steps
        val grid = queue.remove()
        if (grid.totalEnergy >= minEnergy) {
            ++energySkips
            continue
        }
        if (!visited.add(grid)) {
            ++dupeSkips
            continue
        }
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

        fun moveAndEnqueue(src: Point, dest: Point) {
            queue.add(grid.move(src, dest))
        }

        fun moveToHall(start: Point) {
            for (x in (start.x - 1) downTo Grid.HALL_XS.first) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    moveAndEnqueue(start, p)
                }
            }
            for (x in (start.x + 1)..Grid.HALL_XS.last) {
                val p = Point(x, Grid.HALL_Y)
                if (!grid.isOpen(p)) break
                if (grid.canStopAt(p)) {
                    moveAndEnqueue(start, p)
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
                    moveAndEnqueue(start, dest)
                }
            } else if (start.x == breed.homeX) { // home
                if ((start.y..(grid.height - 2)).any { y ->
                        grid[Point(start.x, y)] != breed
                    }) {
                    // blocking another
                    moveToHall(start)
                }
            } else { // wrong room
                if (((start.y - 1) downTo (Grid.HALL_Y + 1)).any { y ->
                        !grid.isOpen(Point(start.x, y))
                    }) {
                    // blocked in
                    return@forEach
                }
                val dest = findDestInRoom(start, breed)
                if (dest != null) {
                    moveAndEnqueue(start, dest)
                } else {
                    moveToHall(start)
                }
            }
        }
    }

    if (minEnergy == Long.MAX_VALUE) {
        throw IllegalStateException("failed to find a solution ($steps steps)?!")
    }

    println(
        """
            %,7d min energy
            %,7d steps
            %,7d energy skips
            %,7d duplicate skips
        """.trimIndent()
            .format(minEnergy, steps, energySkips, dupeSkips)
    )
//    val stack: Deque<Grid> = ArrayDeque()
//    var curr = resultGrid
//    while (curr != null) {
//        stack.push(curr)
//        curr = curr.prev
//    }
//    stack.forEach {
//        printBoxed(it)
//    }
    return minEnergy
}

fun partTwo(input: String): Long {
    val lines = input.lines().toMutableList()
    lines.add(3, "  #D#C#B#A#  ")
    lines.add(4, "  #D#B#A#C#  ")
    return solve(lines)
}
