package day05_hydrothermal_venture

import geom2d.Point
import histogram.Histogram
import histogram.count
import histogram.mutableHistogramOf
import util.printBoxed
import util.saveTextFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.PrintWriter
import kotlin.math.abs
import kotlin.math.max

fun main() {
    util.solve(7644, ::partOne)
    util.solve(18627, ::partTwo)
    draw.saveImage(::draw)
    saveTextFile(::csv_lines, "lines.csv")
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

private fun String.toLines() =
    lineSequence()
        .map(String::toLine)

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
    mutableHistogramOf<Point>()
        .also { hist ->
            input
                .toLines()
                .filter { it.vertical || it.horizontal }
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
//        .also { it.printMap() }
        .count { it.value > 1 }

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
    printBoxed(sb.toString())
}

fun partTwo(input: String) =
    mutableHistogramOf<Point>()
        .also { hist ->
            input.toLines()
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
//        .also { it.printMap() }
        .count { it.value > 1 }


fun draw(input: String, img: BufferedImage) {
    val linesTwo = input
        .toLines()
        .toList()
    val linesOne = linesTwo
        .filter { it.vertical || it.horizontal }
    val histOne = mutableHistogramOf<Point>()
        .also { hist ->
            linesOne
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
    val histTwo = mutableHistogramOf<Point>()
        .also { hist ->
            linesTwo
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
    val width = img.width
    val height = img.height
    img.createGraphics().apply {
        color = Color.YELLOW.darker().darker()
        fillRect(0, 0, width, height)
    }
    img.createGraphics().apply {
        translate(5, 5)
        scale(width / 1010.0, height / 1010.0)
        histTwo.keys
            .groupBy { p ->
                val dn = histTwo[p]!!.toInt()
                val on = histOne.getOrDefault(p, 0).toInt()
                when {
                    dn == 1 && on == 0 ->
                        // non-conflict, diagonal-only
                        Pair(2, Color.BLUE)
                    dn == 1 && on == 1 ->
                        // non-conflict, orthogonal-only
                        Pair(1, Color.GREEN)
                    dn > 1 && on == 0 ->
                        // diagonal conflict, diagonal-only
                        Pair(5, Color.CYAN)
                    dn > 1 && on == 1 ->
                        // diagonal conflict, both lines
                        Pair(4, Color.MAGENTA)
                    dn == on ->
                        // orthogonal conflict
                        Pair(3, Color.YELLOW)
                    dn > on ->
                        // both conflict
                        Pair(6, Color.RED)
                    else -> throw IllegalStateException("Unknown state $dn/$on")
                }
            }
            .toSortedMap { a, b ->
                a.first - b.first
            }
            .forEach { (grp, points) ->
                color = grp.second
                points.forEach { p ->
                    drawOval(p.x.toInt() - 1, p.y.toInt() - 1, 2, 2)
                }
            }
    }
}

private val Line.type
    get() = when {
        horizontal -> "h"
        vertical -> "v"
        fortyFive -> "d"
        else -> throw IllegalStateException("Unknown line type?!")
    }

private fun csv_lines(input: String, out: PrintWriter) {
    out.println("type,x1,y1,x2,y2")
    input
        .toLines()
        .forEach {
            out.println("${it.type},${it.start.x},${it.start.y},${it.end.x},${it.end.y}")
        }
}
