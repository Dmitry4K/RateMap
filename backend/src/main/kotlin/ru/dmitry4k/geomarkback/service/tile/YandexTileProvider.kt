package ru.dmitry4k.geomarkback.service.tile

interface YandexTileProvider {
    fun getTile(x: Int, y: Int, z: Int): ByteArray
}