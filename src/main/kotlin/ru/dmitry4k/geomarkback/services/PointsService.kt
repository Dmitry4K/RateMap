package ru.dmitry4k.geomarkback.services

import ru.dmitry4k.geomarkback.services.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.dto.GeoPointRate

interface PointsService {
    fun getPoints(point: GeoPoint, radius: Double): List<GeoPointRate>
}