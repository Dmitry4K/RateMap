package ru.dmitry4k.geomarkback.service.tile

interface YandexTileService {
    fun getTile(layerName: String, x: Int, y: Int, z: Int): ByteArray
    fun getLegend(layerName: String, width: Int, height: Int, fontSize: Int): ByteArray
}