package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.service.tile.TileModifier

class StepInterpolationModifier(
    private val step: Double
): TileModifier<Double, Double> {
    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
        val xSize = matrix.size
        val ySize = matrix[0].size

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                matrix[x][y] = (matrix[x][y] / step).toInt() * step
            }
        }

        return matrix
    }
}