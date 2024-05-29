package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.dto.Point2D
import ru.dmitry4k.geomarkback.service.tile.TileModifier

class StepInterpolationModifier(
    private val step: Double
): TileModifier<Point2D<Double, Double>, Point2D<Double, Double>> {
    override fun modify(matrix: MutableList<MutableList<Point2D<Double, Double>>>): MutableList<MutableList<Point2D<Double, Double>>> {
        val xSize = matrix.size
        val ySize = matrix[0].size

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                matrix[x][y].x = (matrix[x][y].x / step).toInt() * step
                matrix[x][y].y = (matrix[x][y].y / step).toInt() * step
            }
        }

        return matrix
    }
}