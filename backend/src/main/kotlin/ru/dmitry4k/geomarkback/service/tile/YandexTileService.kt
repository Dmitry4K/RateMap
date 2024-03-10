package ru.dmitry4k.geomarkback.service.tile

interface YandexTileService {
    fun getTile(layerName: String, x: Int, y: Int, z: Int): ByteArray
}