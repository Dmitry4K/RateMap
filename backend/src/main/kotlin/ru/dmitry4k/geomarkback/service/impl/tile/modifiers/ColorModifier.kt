package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.service.tile.TileModifier
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class ColorModifier: TileModifier<Double, Color> {
    private val alphaFloat: Double = 0.7
    private val alphaInt: Int = (alphaFloat * 256).toInt()
    private val minColor: Color = Color(Color.RED.red, Color.RED.green, Color.RED.blue, alphaInt)
    private val maxColor: Color = Color(Color.GREEN.red, Color.GREEN.green, Color.GREEN.blue, alphaInt)
    private val defaultColor = getColor(0.5, minColor, maxColor)

    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Color>> {
        val xSize = matrix.size
        val ySize = matrix[0].size

        val colors = MutableList(xSize) {
            MutableList(ySize) { defaultColor }
        }

        for (x in matrix.indices) {
            for (y in matrix[0].indices) {
                colors[x][y] = getColor(matrix[x][y], minColor, maxColor)
            }
        }

        return colors
    }

    private fun getColor(z: Double, minColor: Color, maxColor: Color): Color {
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
}