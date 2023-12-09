package ru.dmitry4k.geomarkback.service

interface YandexTileService {
    fun getTile(x: Int, y: Int, z: Int): ByteArray
}