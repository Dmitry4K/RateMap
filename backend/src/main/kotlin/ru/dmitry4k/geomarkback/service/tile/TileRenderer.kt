package ru.dmitry4k.geomarkback.service.tile

import ru.dmitry4k.geomarkback.dto.Point4D

interface TileRenderer {
    fun renderTile(points: List<Point4D<Int, Int, Double, Double>>, radius: Int): ByteArray
    fun getTileSize(): Int
}