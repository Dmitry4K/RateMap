package ru.dmitry4k.geomarkback.services.geomark

import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate

interface AreaRateService {
    fun addAreaRate(rate: GeoRate, points: List<GeoPoint>)
}