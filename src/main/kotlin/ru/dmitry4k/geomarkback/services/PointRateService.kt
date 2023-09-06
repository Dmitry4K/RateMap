package ru.dmitry4k.geomarkback.services

import ru.dmitry4k.geomarkback.services.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.dto.GeoPointRate
import ru.dmitry4k.geomarkback.services.dto.GeoRate

interface PointRateService {
    fun addRate(rate: Double, point: GeoPoint): GeoPointRate
}