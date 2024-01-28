package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.TileId
import ru.dmitry4k.geomarkback.service.Distance
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.TileIdMercator
import ru.dmitry4k.geomarkback.service.YandexTileService
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayOutputStream
import java.util.logging.Logger
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

val log = Logger.getLogger("YandexTileServiceImpl")

@Service
class YandexTileServiceImpl(
    val tileIdMercator: TileIdMercator,
    val distance: Distance,
    val markService: MarksService,
) : YandexTileService {
    private val radius = 128
    private val size = 256
    private val alphaFloat = 0.3
    private val alphaInt = (alphaFloat * 256).toInt()
    private val minColor = Color(Color.RED.red, Color.RED.green, Color.RED.blue, alphaInt)
    private val maxColor = Color(Color.GREEN.red, Color.GREEN.green, Color.GREEN.blue, alphaInt)
    private val defaultColor = getColor(0.5, minColor, maxColor)
    override fun getTile(x: Int, y: Int, z: Int): ByteArray {
        val center = tileIdMercator.getPointByTileId(TileId(x + 0.5, y + 0.5, z))
        val maxDistance = listOf(
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y + 1.0, z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y + 1.0, z))
        ).maxOfOrNull { distance.distance(it, center) }!! * 2.0
        println("lat: ${center.lat}, lng: ${center.lng}")
        val points = markService.getMarks(center.lat, center.lng, maxDistance.toLong())
            .map {
                val geoPoint = GeoPoint(it.point!!.y, it.point!!.x)
                val tileId = tileIdMercator.getTileIdByPoint(geoPoint, z)

                XYZDoublePoint(
                    (size * getFractionalPart(tileId.x)).toInt(),
                    (size * getFractionalPart(tileId.y)).toInt(),
                    it.mark!! / 5.0
                )
            }
        return renderTile(points)
    }

    private fun getFractionalPart(a: Double): Double {
        return a - a.toInt().toDouble()
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
                    val componentColor = Color(components[0], components[1], components[2], alphaInt)
                    if (kSum < 1.0) {
                        val color = getColor(kSum, defaultColor, componentColor)
                        bufferedImage.setRGB(x, y, color.rgb)
                    } else {
                        bufferedImage.setRGB(x, y, componentColor.rgb)
                    }
                }
            }
        }

        val baos = ByteArrayOutputStream()
        for (x in size/2 - 5..size/2 + 5) {
            for (y in size/2 - 5..size/2 + 5) {
                bufferedImage.setRGB(x, y, Color.BLACK.rgb)
            }
        }
        ImageIO.write(bufferedImage, "png", baos)
        return baos.toByteArray()
    }

    private fun getColor(z: Double, minColor: Color, maxColor: Color): Color {
        if (z < 0.0 || z > 1.0) {
            log.warning("Invalid z value: $z")
        }
        val red = getColorInt(z, minColor.red, maxColor.red)
        val green = getColorInt(z, minColor.green, maxColor.green)
        val blue = getColorInt(z, minColor.blue, maxColor.blue)
        return Color(red, green, blue, alphaInt)
    }

    private fun getColorInt(z: Double, minColorComponent: Int, maxColorComponent: Int): Int {
        if (z < 0.0 || z > 1.0) {
            throw Exception("Invalid z value: $z")
        }
        return max(min(255, (z * maxColorComponent + (1 - z) * minColorComponent).toInt()), 0)
    }

    private fun dist(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        return sqrt(((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)).toDouble())
    }
}