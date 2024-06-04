package ru.dmitry4k.geomarkback.service.impl.tile

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.tile.LegendRenderer
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min


@Service
class LegendTileRendererImpl: LegendRenderer {
    override fun renderLegend(width: Int, height: Int, gradient: List<Pair<Double, Color>>, fontSize: Int): ByteArray {

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        drawGradient(bufferedImage, gradient)
        drawRuler(bufferedImage, gradient, fontSize)
        val output = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", output)
        return output.toByteArray()
    }

    private fun calculateColor(value: Double, gradient: List<Pair<Double, Color>>): Color {
        val index = max(0, gradient.indexOfFirst { value > it.first })
        val a = gradient[index].second
        val b = gradient[index+1].second
        return calculateColor(value, a, b)
    }

    private fun calculateColor(value: Double, first: Color, second: Color): Color = Color(
        calculateComponent(value, first.red, second.red),
        calculateComponent(value, first.green, second.green),
        calculateComponent(value, first.blue, second.blue),
        200
    )

    private fun calculateComponent(value: Double, first: Int, second: Int): Int {
        return first + (value * (second - first).toDouble()).toInt()
    }

    private fun drawGradient(image: BufferedImage, gradient: List<Pair<Double, Color>>) {
        for (x in 0 until image.width) {
            for (y in 0 until  image.height) {
                image.setRGB(x, y, calculateColor((image.height - y).toDouble().div(image.height.toDouble()), gradient).rgb)
            }
        }
    }

    private fun drawRuler(image: BufferedImage, gradient: List<Pair<Double, Color>>, fontSize: Int) {
        val min = gradient.first().first
        val max = gradient.last().first

        for (e in gradient) {
            image.graphics.apply {
                color = Color.BLACK
                font = Font("Arial", Font.BOLD, fontSize)
                val rawPosition = ((e.first - min) / (max - min) * image.height).toInt()
                val position = max(1, min(image.height - fontSize, rawPosition))
                val string = String.format("%.0f", e.first)
                val width = getFontMetrics(font).stringWidth(string)
                drawString(string, (image.width - width)/2, min(image.height-10, max(10, image.height - position)))
            }
        }
    }
}