package draw

import util.Stopwatch
import util.answer
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.font.LineMetrics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(work: (BufferedImage) -> Unit) {
    val img = BufferedImage(
        1024,
        768,
        BufferedImage.TYPE_INT_RGB
    )
    val watch = Stopwatch()
    work(img)
    val elapsed = watch.elapsed
    val outFile = File("${work.javaClass.packageName}.png")
    ImageIO.write(img, "png", outFile)
    answer(outFile, elapsed, label = "Saved image")
}

fun BufferedImage.antialiasedGraphics(): Graphics2D =
    createGraphics().apply {
        setRenderingHints(
            RenderingHints(
                mapOf(
                    RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                    RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                )
            )
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
