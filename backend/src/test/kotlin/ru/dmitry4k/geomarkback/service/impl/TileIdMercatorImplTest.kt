package ru.dmitry4k.geomarkback.service.impl

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.dmitry4k.geomarkback.service.dto.TileId
import kotlin.math.abs

class TileIdMercatorImplTest {
    private val tileIdMercator = TileIdMercatorImpl(SphericalMercatorImpl())

    @Test
    fun mercatorTest() {
        val tileIds = listOf(
            TileId(10.0, 10.0, 4),
            TileId(0.0, 0.0, 2),
            TileId(5.0, 0.0, 3),
            TileId(19.0, 19.0, 6),
            TileId(13.0, 19.9, 5),
            TileId(3.0, 19.0, 5),
            TileId(4.0, 19.0, 5),
            TileId(1.0, 2.0, 8),
            TileId(3.0, 4.0, 7),
        )
        val restoredTileIds = tileIds.map {
            tileIdMercator.getTileIdByPoint(tileIdMercator.getPointByTileId(it), it.z)
        }
        val epsilon = 0.000001
        tileIds.zip(restoredTileIds).forEach {
            assertTrue(abs(it.first.x - it.second.x) < epsilon, "x is invalid")
            assertTrue(abs(it.first.y - it.second.y) < epsilon, "y is invalid")
            assertTrue(abs(it.first.z - it.second.z) < epsilon, "z is invalid")
        }
    }
}