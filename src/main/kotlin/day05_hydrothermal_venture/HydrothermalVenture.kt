package day05_hydrothermal_venture

import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import geom2d.Point
import histogram.Histogram
import histogram.count
import kotlin.math.abs
import kotlin.math.max

fun main() {
    util.solve(7644, ::partOne)
    util.solve(18627, ::partTwo)
}

data class Line(val start: Point, val end: Point) {

    val horizontal
        get() = start.y == end.y

    val vertical
        get() = start.x == end.x

    val fortyFive
        get() = abs(start.x - end.x) == abs(start.y - end.y)

    private val xseq
        get() = (if (start.x < end.x) start.x..end.x else start.x downTo end.x)
            .asSequence()

    private val yseq
        get() = (if (start.y < end.y) start.y..end.y else start.y downTo end.y)
            .asSequence()

    val allPoints
        get() = when {
            horizontal -> xseq.map { start.copy(x = it) }
            vertical -> yseq.map { start.copy(y = it) }
            fortyFive -> xseq.zip(yseq, ::Point)
            else -> throw UnsupportedOperationException("Unsupported slope for point enumeration.")
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

fun partOne(input: String) =
    HashMap<Point, Int>().let { hist ->
        input
            .lines()
            .map(String::toLine)
            .asSequence()
            .filter { it.vertical || it.horizontal }
            .map(Line::allPoints)
            .forEach { it.forEach(hist::count) }
//        hist.printMap()
        hist.count { it.value > 1 }
    }

@Suppress("unused")
private fun Histogram<Point>.printMap() {
    val (mx, my) = keys.fold(Pair(0L, 0L)) { (mx, my), p ->
        Pair(max(mx, p.x), max(my, p.y))
    }
    val sb = StringBuilder()
    for (y in 0..mx) {
        for (x in 0..my) {
            sb.append(
                when (val n = get((Point(x, y)))) {
                    null -> '.'
                    else -> n
                }
            )
        }
        sb.append('\n')
    }
    Terminal().println(table {
        body {
            row(sb.toString())
        }
    })
}

fun partTwo(input: String) =
    HashMap<Point, Int>().let { hist ->
        input
            .lines()
            .map(String::toLine)
            .asSequence()
            .map(Line::allPoints)
            .forEach { it.forEach(hist::count) }
//        hist.printMap()
        hist.count { it.value > 1 }
    }
