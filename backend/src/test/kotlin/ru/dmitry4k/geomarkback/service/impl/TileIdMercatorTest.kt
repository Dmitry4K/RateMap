package ru.dmitry4k.geomarkback.service.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.TileId
import ru.dmitry4k.geomarkback.service.impl.geo.TileIdMercatorImpl
import ru.dmitry4k.geomarkback.service.impl.geo.Wgs84MercatorImpl

class TileIdMercatorTest {
    private val tileIdMercatorImpl = TileIdMercatorImpl(Wgs84MercatorImpl())

    @Test
    fun getTileByGeoPoint() {
        val tile = TileId(2475.0, 1283.0, 12)
        val newTile = tileIdMercatorImpl.getTileIdByPoint(GeoPoint(
            55.78221704371905,
            37.5732421875
        ), 12)
        Assertions.assertEquals(tile.x.toInt(), newTile.x.toInt())
        Assertions.assertEquals(tile.y.toInt(), newTile.y.toInt())
        Assertions.assertEquals(tile.z, newTile.z)
    }
}