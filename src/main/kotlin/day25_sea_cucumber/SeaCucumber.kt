package day25_sea_cucumber

/**
 * It's a torus!
 */
fun main() {
    util.solve(386, ::partOne)
//    util.solve(::partTwo)
}

private const val OPEN = '.'
private const val EAST = '>'
private const val SOUTH = 'v'

fun partOne(input: String): Int {
    val width = input.indexOf('\n')
    var g = input
        .filter { it != '\n' }
        .toCharArray()
    val height = g.size / width

    var tickCount = 0
    while (true) {
        var moves = 0
        var next = g.copyOf()
        // move east
        for ((i, c) in g.withIndex()) {
            if (c != EAST) continue
            val x = (i % width + 1) % width
            val y = i / width
            val j = y * width + x
            if (g[j] == OPEN) {
                next[j] = EAST
                next[i] = OPEN
                moves++
            }
        }
        g = next
        next = g.copyOf()
        // move south
        for ((i, c) in g.withIndex()) {
            if (c != SOUTH) continue
            val x = i % width
            val y = (i / width + 1) % height
            val j = y * width + x
            if (g[j] == OPEN) {
                next[j] = SOUTH
                next[i] = OPEN
                moves++
            }
        }
        g = next
        ++tickCount
        if (moves == 0) break
    }
    return tickCount
}

fun partTwo(input: String) =
    input.trim().length
