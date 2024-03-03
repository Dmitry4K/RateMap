package ru.dmitry4k.geomarkback.service.impl.tile

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.Point3D
import ru.dmitry4k.geomarkback.service.tile.TileModifier
import ru.dmitry4k.geomarkback.service.tile.TileRenderer
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.sqrt

@Primary
@Service
class YandexTileRendererImpl2(
    private val tileModifier: TileModifier<Double, Color>
): TileRenderer {
    private val size: Int = 512
    override fun renderTile(points: List<Point3D<Int, Int, Double>>, radius: Int): ByteArray {
        val doubles = MutableList(size) {
            MutableList(size) { 0.5 }
        }
        for (x in doubles.indices) {
            for (y in doubles[0].indices) {
                val filteredPoints = points.filter { dist(it.x, it.y, x, y) <= radius }
                val k = filteredPoints.map { 1 - dist(x, y, it.x, it.y) / radius.toDouble() }
                val kw = filteredPoints.zip(k).map { (p, k) -> p.z * k }
                doubles[x][y] =  kw.sum() / k.sum()
            }
        }

        val colors = tileModifier.modify(doubles)
        val bufferedImage = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        for (x in colors.indices) {
            for (y in colors[0].indices) {
                bufferedImage.setRGB(x, y, colors[x][y].rgb)
            }
        }
        val output = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", output)
        return output.toByteArray()
    }

    override fun getTileSize(): Int = size

    private fun dist(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        return sqrt(((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)).toDouble())
    }
}