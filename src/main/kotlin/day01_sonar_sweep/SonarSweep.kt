package day01_sonar_sweep

import java.awt.Color
import java.awt.Polygon
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
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
    val count = depths.size.toFloat() - 1
    val max = depths.reduce(::max).toFloat()
    val zero = 0f
    val width = img.width.toFloat()
    val height = img.height.toFloat()
    val sky = 50f
    val waves = 1f
    val sea = height - sky - waves
    val g = img.createGraphics()
    val rh = RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    )
    g.setRenderingHints(rh)
    g.color = COLOR_SKY
    g.fill(Rectangle2D.Float(zero, zero, width, sky))
    g.color = Color.WHITE
    g.fill(Rectangle2D.Float(zero, sky, width, waves))
    g.color = Color.YELLOW.darker()
    g.fill(Rectangle2D.Float(zero, sky + waves, width, sea))
    g.color = Color.YELLOW.brighter()
    (sky / 1.5).toInt().let {
        g.fillOval(-it / 2, -it / 2, it, it)
    }

    val water = Polygon()
    val dx = width / count
    val dy = sea / max
    depths
        .mapIndexed { i, d -> Pair(i * dx, sky + waves + d * dy) }
        .forEach { (x, y) -> water.addPoint((width - x).toInt(), y.toInt()) }
    water.addPoint(zero.toInt(), (sky + waves).toInt())
    water.addPoint(width.toInt(), (sky + waves).toInt())
    g.color = Color.BLUE
    g.fill(water)

    val ship = Polygon()
    ship.addPoint(width.toInt(), sky.toInt() - 16)
    ship.addPoint(width.toInt() - 7, sky.toInt() - 16)
    ship.addPoint(width.toInt() - 10, sky.toInt() - 10)
    ship.addPoint(width.toInt() - 20, sky.toInt() - 10)
    ship.addPoint(width.toInt() - 15, (sky + waves + 2).toInt())
    ship.addPoint(width.toInt(), (sky + waves + 2).toInt())
    g.color = Color.WHITE.darker()
    g.fill(ship)

    repeat(5) { i ->
        (i * 6).let { d ->
            g.drawArc(
                (width - 25).toInt() - d,
                (sky + waves - 20).toInt(),
                30 + d,
                30 + d,
                210 - d / 2,
                30 + d
            )
        }
    }
}
