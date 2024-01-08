package ru.dmitry4k.geomarkback.service.impl

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.dmitry4k.geomarkback.service.dto.GeoPoint
import kotlin.math.abs

class SphericalMercatorImplTest {
    private val mercator = SphericalMercatorImpl()

    @Test
    fun mercatorTest() {
        val geoPoints = listOf(
            GeoPoint(-90.0, 180.0),
            GeoPoint(90.0, 180.0),
            GeoPoint(-90.0, -180.0),
            GeoPoint(90.0, -180.0),
            GeoPoint(0.0, 0.0),
            GeoPoint(40.0, 50.0),
            GeoPoint(-30.0, 90.0),
        )
        val restoredGeoPoints = geoPoints.map {
            mercator.xyToPoint(mercator.pointToXY(it))
        }
        val epsilon = 0.0000001

        geoPoints.zip(restoredGeoPoints).forEach {
            assertTrue(abs(it.first.lat - it.second.lat) < epsilon)
            assertTrue(abs(it.first.lng - it.second.lng) < epsilon)
        }
    }
}