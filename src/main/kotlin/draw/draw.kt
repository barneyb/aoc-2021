package draw

import util.Stopwatch
import util.answer
import util.getInput
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(solver: (String, BufferedImage) -> Unit) {
    val img = BufferedImage(
        1024,
        768,
        BufferedImage.TYPE_INT_RGB
    )
    val watch = Stopwatch()
    solver(getInput(solver.javaClass), img)
    val elapsed = watch.elapsed
    val outFile = File("${solver.javaClass.packageName}.png")
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
