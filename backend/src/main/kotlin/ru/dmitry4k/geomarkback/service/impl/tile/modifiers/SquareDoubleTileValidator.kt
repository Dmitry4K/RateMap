package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.dto.Point2D
import ru.dmitry4k.geomarkback.service.tile.TileModifier
import kotlin.math.max
import kotlin.math.min

class SquareDoubleTileValidator(
    private val size: Int
) : TileModifier<Point2D<Double, Double>, Point2D<Double, Double>> {
    override fun modify(matrix: MutableList<MutableList<Point2D<Double, Double>>>): MutableList<MutableList<Point2D<Double, Double>>> {
        if (matrix.isEmpty()) {
            throw Exception("Bad X size of matrix")
        }
        val xSize = matrix.size
        val ySize = matrix[0].size

        if (xSize != size || ySize != size) {
            throw Exception("X and Y sizes must be equal")
        }

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                if (matrix[x][y].x < 0 || matrix[x][y].x > 1.0) {
                    matrix[x][y].x = min(1.0, max(0.0, matrix[x][y].x))
                    //throw Exception("All values must be from 0.0 to 1.0 but got: ${matrix[x][y]}")
                }
                if (matrix[x][y].y < 0 || matrix[x][y].y > 1.0) {
                    matrix[x][y].y = min(1.0, max(0.0, matrix[x][y].y))
                    //throw Exception("All values must be from 0.0 to 1.0 but got: ${matrix[x][y]}")
                }
            }
        }
        return matrix
    }
}