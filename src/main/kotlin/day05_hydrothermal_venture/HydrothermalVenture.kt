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
    saveTextFile(::csv_vents, "vents.csv")
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

enum class VentType(val tag: String) {
    ORTHOGONAL("orthogonal"),
    DIAGONAL("diagonal"),
    OVERLAP_ORTHOGONAL("Orthogonal"),
    OVERLAP_COMBO("Combination"),
    OVERLAP_DIAGONAL("Diagonal"),
    OVERLAP_BOTH("Both")
}

data class VentInfo(
    val p: Point,
    val type: VentType,
    val orthCount: Int,
    val diagCount: Int
) {

    val count = orthCount + diagCount

}

fun String.toVentTypes(): Collection<VentInfo> {
    val allLines = toLines()
        .toList()
    val histOne = mutableHistogramOf<Point>()
        .also { hist ->
            allLines
                .filter { it.vertical || it.horizontal }
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
    val histTwo = mutableHistogramOf<Point>()
        .also { hist ->
            allLines
                .map(Line::allPoints)
                .forEach { it.forEach(hist::count) }
        }
    return histTwo.entries
        .map { (p, dnL) ->
            val dn = dnL.toInt()
            val on = histOne.getOrDefault(p, 0).toInt()
            VentInfo(
                p,
                when {
                    dn == 1 && on == 0 -> VentType.DIAGONAL
                    dn == 1 && on == 1 -> VentType.ORTHOGONAL
                    dn > 1 && on == 0 -> VentType.OVERLAP_DIAGONAL
                    dn > 1 && on == 1 -> VentType.OVERLAP_COMBO
                    dn == on -> VentType.OVERLAP_ORTHOGONAL
                    dn > on -> VentType.OVERLAP_BOTH
                    else -> throw IllegalStateException("Unknown state $dn/$on")
                }, on, dn - on
            )
        }
        .sortedWith { a, b ->
            val ap = a.p
            val bp = b.p
            if (ap.y < bp.y)
                -1
            else if (ap.y > bp.y)
                1
            else
                (ap.x - bp.x).toInt()
        }
}

fun draw(input: String, img: BufferedImage) {
    val ventTypes = input.toVentTypes()
    val width = img.width
    val height = img.height
    img.createGraphics().apply {
        color = Color.YELLOW.darker().darker()
        fillRect(0, 0, width, height)
    }
    img.createGraphics().apply {
        translate(5, 5)
        scale(width / 1010.0, height / 1010.0)
        ventTypes
            .groupBy(VentInfo::type)
            .toSortedMap()
            .forEach { (grp, infos) ->
                color = when (grp!!) {
                    VentType.ORTHOGONAL -> Color.GREEN
                    VentType.DIAGONAL -> Color.BLUE
                    VentType.OVERLAP_ORTHOGONAL -> Color.YELLOW
                    VentType.OVERLAP_COMBO -> Color.MAGENTA
                    VentType.OVERLAP_DIAGONAL -> Color.CYAN
                    VentType.OVERLAP_BOTH -> Color.RED
                }
                infos.forEach { i ->
                    i.p.also { p ->
                        drawOval(p.x.toInt() - 1, p.y.toInt() - 1, 2, 2)
                    }
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

private fun csv_vents(input: String, out: PrintWriter) {
    out.println("x,y,type,count,orth_count,diag_count")
    input.toVentTypes()
        .asSequence()
        .filter { it.count > 1 }
        .forEach {
            out.println("${it.p.x},${it.p.y},${it.type.tag},${it.count},${it.orthCount},${it.diagCount}")
        }
}
