package ru.dmitry4k.geomarkback.services.impl

import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import ru.dmitry4k.geomarkback.services.AreaRateService
import ru.dmitry4k.geomarkback.services.PointRateService
import ru.dmitry4k.geomarkback.services.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.dto.GeoRate

@Component
@Validated
class AreaRateServiceImpl(
    val pointRateService: PointRateService
) : AreaRateService {


    override fun addAreaRate(rate: Double, points: List<GeoPoint>) {
//        when (points.size) {
//            0 -> pointRateService.addRate(rate, points.first())
//            else -> addAreaRateInt(rate, points)
//        }
    }

    private fun addAreaRateInt(rate: GeoRate, points: List<GeoPoint>) {
        TODO()
    }
}