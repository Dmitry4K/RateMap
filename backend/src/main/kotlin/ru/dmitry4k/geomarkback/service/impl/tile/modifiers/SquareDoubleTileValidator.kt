package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.service.tile.TileModifier
import kotlin.math.max
import kotlin.math.min

class SquareDoubleTileValidator : TileModifier<Double, Double> {
    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
        if (matrix.isEmpty()) {
            throw Exception("Bad X size of matrix")
        }
        val xSize = matrix.size
        val ySize = matrix[0].size

        if (xSize != ySize) {
            throw Exception("X and Y sizes must be equal for border modifier")
        }

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                if (matrix[x][y] < 0 || matrix[x][y] > 1.0) {
                    matrix[x][y] = min(1.0, max(0.0, matrix[x][y]))
                    //throw Exception("All values must be from 0.0 to 1.0 but got: ${matrix[x][y]}")
                }
            }
        }
        return matrix
    }
}