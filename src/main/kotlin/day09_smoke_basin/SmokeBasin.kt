package day09_smoke_basin

import geom2d.Point

fun main() {
    util.solve(554, ::partOne) // 1774 is too high
    util.solve(::partTwo)
}

fun partOne(input: String): Int {
    val width = input.indexOf('\n')
    val grid = input
        .filter { it != '\n' }
    val height = grid.length / width
    val lowPoints = mutableListOf<Point>()
    for (r in 0 until height) {
        for (c in 0 until width) {
            val n = grid[r * width + c]
            if (r > 0 && n >= grid[(r - 1) * width + c]) continue
            if (r < height - 1 && n >= grid[(r + 1) * width + c]) continue
            if (c > 0 && n >= grid[r * width + c - 1]) continue
            if (c < width - 1 && n >= grid[r * width + c + 1]) continue
            lowPoints.add(Point(c.toLong(), r.toLong()))
        }
    }
    return lowPoints
        .map { p -> grid[(p.y * width + p.x).toInt()] }
        .sumOf { it.digitToInt() + 1 }
}

fun partTwo(input: String) = input.trim().length
