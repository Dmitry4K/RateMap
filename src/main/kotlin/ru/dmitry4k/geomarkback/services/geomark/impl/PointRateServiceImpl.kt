package ru.dmitry4k.geomarkback.services.geomark.impl

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.services.geomark.PointRateService
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.geomark.dto.GeoRate

@Component
class PointRateServiceImpl : PointRateService {
    override fun addRate(rate: GeoRate, point: GeoPoint) {
        TODO("Not yet implemented")
    }
}