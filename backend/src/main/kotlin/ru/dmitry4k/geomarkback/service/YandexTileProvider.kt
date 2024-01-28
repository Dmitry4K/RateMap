package ru.dmitry4k.geomarkback.service

interface YandexTileProvider {
    fun getTile(x: Int, y: Int, z: Int): ByteArray
}