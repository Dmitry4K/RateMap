package ru.dmitry4k.geomarkback.services.geomark

import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPointRate

interface PointsService {
    fun getPoints(point: GeoPoint, radius: Double): List<GeoPointRate>
}