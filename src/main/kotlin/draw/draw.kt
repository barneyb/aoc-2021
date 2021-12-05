package draw

import util.getInput
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun saveImage(solver: (String, BufferedImage) -> Unit) {
    val img = BufferedImage(
        1024,
        768,
        BufferedImage.TYPE_INT_RGB
    )
    solver(getInput(solver.javaClass), img)
    ImageIO.write(img, "png", File("${solver.javaClass.packageName}.png"))
}
