package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.TileId

interface TileIdMercator {
    fun getPointByTileId(tileId: TileId): GeoPoint

    fun getTileIdByPoint(geoPoint: GeoPoint, z: Int): TileId
}