package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.YandexTileService
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

@Service
class YandexTileServiceImpl : YandexTileService {
    private val radius = 32
    private val size = 128
    private val alphaFloat = 0.3
    private val alphaInt = (alphaFloat * 256).toInt()
    private val minColor = Color(Color.RED.red, Color.RED.green, Color.RED.blue, alphaInt)
    private val maxColor = Color(Color.GREEN.red, Color.GREEN.green, Color.GREEN.blue, alphaInt)
    private val defaultColor = getColor(0.5, minColor, maxColor)
    override fun getTile(x: Int, y: Int, z: Int): ByteArray {
        val points = mutableListOf<XYZDoublePoint>()
        val step = (radius *1.2).toInt()
        var i = 0
        for (cX in 0 until size + radius step step) {
            i++
            for (cY in (step / 2 * (i % 2)) until size + radius step step) {
                points.add(XYZDoublePoint(cX, cY, Random.nextDouble()))
            }
        }
        return renderTile(points)
    }
    data class XYZDoublePoint(val x: Int, val y: Int, val z: Double)
    data class XYColor(val x: Int, val y: Int, val color: Color)

    private fun renderTile(points: List<XYZDoublePoint>): ByteArray {
        val pointsAndColors = points.map { XYColor(it.x ,it.y, getColor(it.z, minColor, maxColor)) }
        val bufferedImage = BufferedImage(size, size, TYPE_INT_ARGB)
        for (x in 0 until size) {
            for (y in 0 until size) {
                val filteredPoints = pointsAndColors.filter { dist(it.x , it.y, x,  y) <= radius }
                if (filteredPoints.isEmpty()) {
                    bufferedImage.setRGB(x, y, defaultColor.rgb)
                } else {
                    val sums = filteredPoints
                        .map {
                            val k = 1 - dist(x, y, it.x, it.y) / radius.toDouble()
                            listOf(
                                k * it.color.red,
                                k * it.color.green,
                                k * it.color.blue,
                                k
                            )
                        }
                        .reduce { a, i -> a.zip(i) { f, s -> f + s } }
                    val kSum = sums[3]
                    val components = sums.subList(0, 3).map { it / kSum }.map(Double::toInt)
                    val color = getColor(1 - kSum, Color(components[0], components[1], components[2]), defaultColor)
                    bufferedImage.setRGB(x, y, color.rgb)
                }
            }
        }

        val baos = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", baos)
        return baos.toByteArray()
    }

    private fun getColor(z: Double, minColor: Color, maxColor: Color): Color {
        if (z < 0.0 || z > 1.0) {
            throw Exception("Invalid z value")
        }
        val red = getColorInt(z, minColor.red, maxColor.red)
        val green = getColorInt(z, minColor.green, maxColor.green)
        val blue = getColorInt(z, minColor.blue, maxColor.blue)
        return Color(red, green, blue, alphaInt)
    }

    private fun getColorInt(z: Double, minColorComponent: Int, maxColorComponent: Int): Int {
        if (z < 0.0 || z > 1.0) {
            throw Exception("Invalid z value")
        }
        return max(min(255, (z * maxColorComponent + (1 - z) * minColorComponent).toInt()), 0)
    }

    private fun dist(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        return sqrt(((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)).toDouble())
    }
}