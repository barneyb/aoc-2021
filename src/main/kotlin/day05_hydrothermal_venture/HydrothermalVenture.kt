package day05_hydrothermal_venture

import geom2d.Point
import histogram.count

fun main() {
    util.solve(7644, ::partOne)
    util.solve(::partTwo)
}

data class Line(val start: Point, val end: Point) {

    val horizontal
        get() = start.y == end.y

    val vertical
        get() = start.x == end.x

    val allPoints
        get() = when {
            horizontal -> (if (start.x < end.x) start.x..end.x else start.x downTo end.x)
                .asSequence()
                .map { start.copy(x = it) }
            vertical -> (if (start.y < end.y) start.y..end.y else start.y downTo end.y)
                .asSequence()
                .map { start.copy(y = it) }
            else -> throw UnsupportedOperationException("Only vertical and horizontal lines can have their points enumerated.")
        }
}

private fun String.toLine() =
    replace(" -> ", ",")
        .split(',')
        .map(String::toLong)
        .let {
            Line(
                Point(it[0], it[1]),
                Point(it[2], it[3]),
            )
        }

fun partOne(input: String): Int {
    val lines = input
        .lines()
        .map(String::toLine)
    val hist = HashMap<Point, Int>()
        .also { hist ->
            lines
                .asSequence()
                .filter { it.vertical || it.horizontal }
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
//    val (mx, my) = lines.fold(Pair(0L, 0L)) { (mx, my), l ->
//        Pair(
//            max(mx, max(l.start.x, l.end.x)),
//            max(my, max(l.start.y, l.end.y)),
//        )
//    }
//    for (y in 0..mx) {
//        for (x in 0..my) {
//            print(
//                when (val n = hist[(Point(x, y))]) {
//                    null -> '.'
//                    else -> n
//                }
//            )
//        }
//        println()
//    }
    return hist.count { it.value > 1 }
}

fun partTwo(input: String) = input.trim().length
