package ru.dmitry4k.geomarkback.service.impl

import ru.dmitry4k.geomarkback.service.Distance
import ru.dmitry4k.geomarkback.dto.GeoPoint
import kotlin.math.cos
import kotlin.math.sqrt

class SphericalDistanceImpl(
    private val radius: Double
) : Distance {
    override fun distance(a: GeoPoint, b: GeoPoint): Long {
        val lat1Rad = Math.toRadians(a.lat)
        val lat2Rad = Math.toRadians(b.lat)
        val lon1Rad = Math.toRadians(a.lng)
        val lon2Rad = Math.toRadians(b.lng)

        val x = (lon2Rad - lon1Rad) * cos((lat1Rad + lat2Rad) / 2)
        val y = lat2Rad - lat1Rad
        val distance = sqrt(x * x + y * y) * radius

        return distance.toLong()
    }
}