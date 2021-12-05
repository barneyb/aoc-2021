package day01_sonar_sweep

import day02_dive.Coords1
import day02_dive.Coords2
import day02_dive.trace
import draw.antialiasedGraphics
import util.getInput
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.font.LineMetrics
import java.awt.image.BufferedImage
import java.util.*
import kotlin.math.max

fun main() {
    util.solve(1616, ::partOneFold)
    util.solve(1616, ::partOneZipSeq)
    util.solve(1616, ::partOneZip)
    util.solve(1616, ::partOneLoop)
    util.solve(1645, ::partTwo)
    util.solve(1645, ::partTwoZip)
    draw.saveImage(::draw)
}

fun partOneFold(input: String) =
    input
        .lineSequence()
        .map { it.toInt() }
        .fold(Pair(0, Int.MAX_VALUE)) { (n, prev), it ->
            Pair(if (it > prev) n + 1 else n, it)
        }
        .first

fun partOneLoop(input: String): Int {
    var prev = Int.MAX_VALUE
    var count = 0
    for (it in input.lineSequence()) {
        val n = it.toInt()
        if (n > prev) {
            count += 1
        }
        prev = n
    }
    return count
}

fun partOneZipSeq(input: String) =
    input
        .lineSequence()
        .map { it.toInt() }
        .zipWithNext()
        .count { (a, b) -> a < b }

fun partOneZip(input: String) =
    input
        .lines()
        .map { it.toInt() }
        .zipWithNext()
        .filter { (a, b) -> a < b }
        .size

fun partTwo(input: String): Int {
    var prev = Int.MAX_VALUE
    var curr = 0
    var count = 0
    val buffer: Queue<Int> = LinkedList()
    for (it in input.lineSequence()) {
        val n = it.toInt()
        buffer.add(n)
        curr += n
        if (buffer.size > 3) {
            curr -= buffer.remove()
        }
        if (buffer.size == 3) {
            if (curr > prev) {
                count += 1
            }
            prev = curr
        }
    }
    return count
}

fun partTwoZip(input: String) =
    input
        .lineSequence()
        .map { it.toInt() }
        .windowed(3)
        .map { it.sum() }
        .zipWithNext()
        .count { (a, b) -> a < b }

private val COLOR_SKY = Color.getHSBColor(0.5f, 0.25f, 1f)

fun draw(input: String, img: BufferedImage) {
    val depths = input
        .lines()
        .map { it.toInt() }
    val count = depths.size
    val max = depths.reduce(::max)
    val width = img.width
    val height = img.height
    val sky = 50
    val waves = 1
    val sea = height - sky - waves
    // environment
    val g = img.antialiasedGraphics().apply {
        color = COLOR_SKY
        fillRect(0, 0, width, sky)
        color = Color.WHITE
        fillRect(0, sky, width, waves)
        color = Color.YELLOW.darker().darker()
        fillRect(0, sky + waves, width, sea)
        color = Color.YELLOW.brighter()
        (sky / 1.5).toInt().let {
            fillOval(-it / 2, -it / 2, it, it)
        }
    }

    // water
    img.antialiasedGraphics().apply {
        translate(0, sky + waves)
        scale(width.toDouble() / count, sea.toDouble() / max)
        val water = Polygon().apply {
            depths
                .forEachIndexed { x, y -> addPoint(count - x, y) }
            addPoint(-count, max)
            addPoint(-count, 0)
            addPoint(count, 0)
        }
        color = Color.BLUE.darker()
        fill(water)
    }

    // Day 2
    getInput("day02_dive").let { input02 ->
        val traceOne = Coords1().trace(input02).toList()
        val traceTwo = Coords2().trace(input02).toList()
        img.antialiasedGraphics().apply {
            drawTextBox(
                "Dive!\npart 1",
                10,
                90,
            )
            translate(0, sky + waves)
            scale(width.toDouble() / count, height.toDouble() / max)
            color = Color.GREEN
            stroke = BasicStroke(5f)
            traceOne.last().also {
                val delta = 10
                drawLine(
                    count - it.pos - delta,
                    it.depth - delta,
                    count - it.pos + delta,
                    it.depth + delta
                )
                drawLine(
                    count - it.pos + delta,
                    it.depth - delta,
                    count - it.pos - delta,
                    it.depth + delta
                )
            }
            Polygon()
                .apply {
                    traceOne.forEach { addPoint(count - it.pos, it.depth) }
                }
                .let {
                    drawPolyline(it.xpoints, it.ypoints, it.npoints)
                }
        }
        // draw what's present of trace two
        img.antialiasedGraphics().apply {
            drawTextBox(
                "Dive!\npart 2",
                width - 60,
                height - 40,
                bgColor = Color.WHITE.darker(),
                color = Color.RED.darker()
            )
            translate(0, sky + waves)
            scale(width.toDouble() / count, height.toDouble() / max)
            color = Color.RED
            Polygon()
                .apply {
                    traceTwo
                        .takeWhile { it.depth <= max }
                        .forEach { addPoint(count - it.pos, it.depth) }
                }
                .let {
                    drawPolyline(it.xpoints, it.ypoints, it.npoints)
                }
        }
    }

    // ship
    val ship = Polygon().apply {
        addPoint(width, sky - 15)
        addPoint(width - 7, sky - 15)
        addPoint(width - 10, sky - 8)
        addPoint(width - 20, sky - 8)
        addPoint(width - 15, sky + waves + 3)
        addPoint(width, sky + waves + 3)
    }
    g.color = Color.WHITE.darker()
    g.fill(ship)

    // sonar pulses
    repeat(5) { i ->
        (i * 6).let { d ->
            g.drawArc(
                width - 25 - d,
                sky + waves - 20,
                30 + d,
                30 + d,
                210 - d / 2,
                30 + d
            )
        }
    }

    // info
    g.drawTextBox(
        "Max Depth: $max",
        40,
        height - 25
    )
}

private data class LineWithMetrics(val str: String, val metrics: LineMetrics)

fun Graphics2D.drawTextBox(
    text: String,
    x: Int,
    y: Int,
    bgColor: Color = Color.WHITE,
    color: Color = Color.BLACK
) {
    val lines = text.trimIndent().lines()
    val linesWithMetrics = lines.zip(lines.map {
        fontMetrics.getLineMetrics(it, this)
    }, ::LineWithMetrics)
    val totalHeight = linesWithMetrics
        .sumOf { it.metrics.height.toInt() }
    val maxWidth = lines.maxOf(fontMetrics::stringWidth)
    this.color = bgColor
    fillRect(x, y, maxWidth + 10, totalHeight + 5)
    this.color = color
    drawRect(x, y, maxWidth + 10, totalHeight + 5)
    linesWithMetrics.fold(0) { yOffset, (line, metrics) ->
        val next = yOffset + metrics.height.toInt()
        drawString(line, x + 5, y + next)
        next
    }
}
