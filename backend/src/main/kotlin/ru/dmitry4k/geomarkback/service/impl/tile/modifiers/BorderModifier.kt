package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import ru.dmitry4k.geomarkback.service.tile.TileModifier
import java.awt.Color
import java.util.*

class BorderModifier(
    private val borderColor: Color
): TileModifier<Color, Color> {
    override fun modify(matrix: MutableList<MutableList<Color>>): MutableList<MutableList<Color>> {
        val xSize = matrix.size
        val ySize = matrix[0].size
        val borderTileIndexes = LinkedList<Pair<Int, Int>>()

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                if (isBorderPoint(x, y, matrix)) {
                    borderTileIndexes.add(x to y)
                }
            }
        }

        borderTileIndexes.forEach { (x, y) -> matrix[x][y] = borderColor }
        return matrix
    }

    private fun isBorderPoint(x: Int, y: Int, matrix: List<List<Color>>) =
        listOf(x - 1 to y - 1, x to y - 1, x - 1 to y, x to y)
            .filter { (xInt , yInt) -> xInt in matrix.indices && yInt in matrix[0].indices }
            .map { (xInt, yInt) -> matrix[xInt][yInt] }
            .any { it.red != matrix[x][y].red || it.green != matrix[x][y].green || it.blue != matrix[x][y].blue }
}