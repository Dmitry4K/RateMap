package ru.dmitry4k.geomarkback.service

import java.awt.Color

interface TileRenderer {
    data class XYZDoublePoint(val x: Int, val y: Int, val z: Double)
    data class XYColor(val x: Int, val y: Int, val color: Color)
    fun renderTile(points: List<XYZDoublePoint>, radius: Int): ByteArray
}