package ru.dmitry4k.geomarkback.service.tile

import java.awt.Color

interface LegendRenderer {
    fun renderLegend(width: Int, height: Int, gradient: List<Pair<Double, Color>>, fontSize: Int): ByteArray
}