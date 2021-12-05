package day01_sonar_sweep

import draw.antialiasedGraphics
import java.awt.Color
import java.awt.Polygon
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
        color = Color.YELLOW.darker()
        fillRect(0, sky + waves, width, sea)
        color = Color.YELLOW.brighter()
        (sky / 1.5).toInt().let {
            fillOval(-it / 2, -it / 2, it, it)
        }
    }

    // water
    img.antialiasedGraphics().apply {
        translate(width / 2, (sky + waves))
        scale(width.toDouble() / count, sea.toDouble() / max)
        val water = Polygon().apply {
            depths
                .forEachIndexed { x, y -> addPoint(width - x, y) }
            addPoint(-width, max)
            addPoint(-width, 0)
            addPoint(width, 0)
        }
        color = Color.BLUE
        fill(water)
    }

    // ship
    val ship = Polygon().apply {
        addPoint(width, sky - 16)
        addPoint(width - 7, sky - 16)
        addPoint(width - 10, sky - 10)
        addPoint(width - 20, sky - 10)
        addPoint(width - 15, sky + waves + 2)
        addPoint(width, sky + waves + 2)
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
    g.apply {
        color = Color.WHITE
        fillRect(15, sky + waves + 15, 120, 40)
        color = Color.BLACK
        drawRect(15, sky + waves + 15, 120, 40)
        drawString("Readings:", 20, sky + waves + 30)
        drawString(count.toString(), 100, sky + waves + 30)
        drawString("Max Depth:", 20, sky + waves + 50)
        drawString(max.toString(), 100, sky + waves + 50)
    }
}
