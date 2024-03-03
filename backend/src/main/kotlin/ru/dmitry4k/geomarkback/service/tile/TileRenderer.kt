package ru.dmitry4k.geomarkback.service.tile

import ru.dmitry4k.geomarkback.dto.Point3D

interface TileRenderer {
    fun renderTile(points: List<Point3D<Int, Int, Double>>, radius: Int): ByteArray
    fun getTileSize(): Int
}