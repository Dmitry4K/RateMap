package ru.dmitry4k.geomarkback.services.geomark

import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPointRate
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate

interface PointRateService {
    fun addRate(rate: Double, point: GeoPoint): GeoPointRate
}