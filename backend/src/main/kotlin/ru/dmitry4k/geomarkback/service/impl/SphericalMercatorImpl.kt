package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.service.Mercator
import ru.dmitry4k.geomarkback.service.dto.GeoPoint
import ru.dmitry4k.geomarkback.service.dto.XYPoint
import kotlin.math.*

private const val RADIUS_MAJOR = 6378137.0

@Component
class SphericalMercatorImpl : Mercator {
    override fun pointToXY(geoPoint: GeoPoint)= with(geoPoint) {
        XYPoint(xAxisProjection(lng), yAxisProjection(lat))
    }

    override fun xyToPoint(xyPoint: XYPoint) = with(xyPoint) {
        GeoPoint(latAxisProjection(y), lngAxisProjection(x))
    }

    override fun distance(a: GeoPoint, b: GeoPoint): Long {
        val lat1Rad = Math.toRadians(a.lat)
        val lat2Rad = Math.toRadians(b.lat)
        val lon1Rad = Math.toRadians(a.lng)
        val lon2Rad = Math.toRadians(b.lng)

        val x = (lon2Rad - lon1Rad) * cos((lat1Rad + lat2Rad) / 2)
        val y = lat2Rad - lat1Rad
        val distance = sqrt(x * x + y * y) * RADIUS_MAJOR

        return distance.toLong()
    }
    private fun xAxisProjection(lng: Double) = Math.toRadians(lng) * RADIUS_MAJOR

    private fun yAxisProjection(lat: Double): Double {
        return ln(tan(Math.PI / 4.0 + Math.toRadians(lat) / 2.0)) * RADIUS_MAJOR
    }

    private fun lngAxisProjection(x: Double) = Math.toDegrees(x / RADIUS_MAJOR)

    private fun latAxisProjection(y: Double): Double {
        return Math.toDegrees(
            2.0 * atan(exp(y / RADIUS_MAJOR)) - Math.PI / 2.0
        )
    }
}