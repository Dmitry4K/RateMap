package ru.dmitry4k.geomarkback.service.impl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.dmitry4k.geomarkback.dto.GeoPoint
import kotlin.math.abs

class Wgs84MercatorImplTest {
    private val mercator = Wgs84MercatorImpl()

    @Test
    fun mappingTest() {
        val geoPoints = listOf(
            GeoPoint(-50.0, 180.0),
            GeoPoint(43.0, 180.0),
            GeoPoint(-67.0, -180.0),
            GeoPoint(43.0, -180.0),
            GeoPoint(0.0, 0.0),
            GeoPoint(40.0, 50.0),
            GeoPoint(-30.0, 90.0),
        )
        val restoredGeoPoints = geoPoints.map {
            mercator.xyToPoint(mercator.pointToXY(it))
        }
        val epsilon = 0.0000001

        geoPoints.zip(restoredGeoPoints).forEach {
            Assertions.assertTrue(abs(it.first.lat - it.second.lat) < epsilon)
            Assertions.assertTrue(abs(it.first.lng - it.second.lng) < epsilon)
        }
    }
}