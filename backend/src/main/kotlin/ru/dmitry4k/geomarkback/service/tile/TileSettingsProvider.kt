package ru.dmitry4k.geomarkback.service.tile

import java.awt.Color

interface TileSettingsProvider {
    fun getTileSize(): Int
    fun getLowestColor(): Color
    fun getHighestColor(): Color
    fun getBorderColor(): Color
    fun getBorderWidth(): Int
    fun getStepValue(): Double
}