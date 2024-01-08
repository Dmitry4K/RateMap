package ru.dmitry4k.geomarkback.service.impl

import ru.dmitry4k.geomarkback.service.Mercator
import ru.dmitry4k.geomarkback.service.dto.GeoPoint
import ru.dmitry4k.geomarkback.service.dto.XYPoint
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.tan

private const val RADIUS_MAJOR = 6378137.0
class SphericalMercatorImpl : Mercator {
    override fun pointToXY(geoPoint: GeoPoint)= with(geoPoint) {
        XYPoint(xAxisProjection(lng), yAxisProjection(lat))
    }

    override fun xyToPoint(xyPoint: XYPoint) = with(xyPoint) {
        GeoPoint(latAxisProjection(y), lngAxisProjection(x))
    }
    private fun xAxisProjection(lng: Double) = Math.toRadians(lng) * RADIUS_MAJOR

    private fun yAxisProjection(lat: Double): Double {
        return ln(tan(Math.PI / 4.0 + Math.toRadians(lat) / 2.0)) * RADIUS_MAJOR
    }

    private fun lngAxisProjection(x: Double) = Math.toDegrees(x / RADIUS_MAJOR)

    private fun latAxisProjection(y: Double): Double {
        return Math.toDegrees(
            2.0 * atan(exp(y/ RADIUS_MAJOR)) - Math.PI / 2.0
        )
    }
}