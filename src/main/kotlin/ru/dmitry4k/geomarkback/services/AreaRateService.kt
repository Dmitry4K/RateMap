package ru.dmitry4k.geomarkback.services

import ru.dmitry4k.geomarkback.services.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.dto.GeoRate

interface AreaRateService {
    fun addAreaRate(rate: Double, points: List<GeoPoint>)
}