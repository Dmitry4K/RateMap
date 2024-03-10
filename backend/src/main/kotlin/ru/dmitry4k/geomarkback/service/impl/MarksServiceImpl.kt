package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.AvgValue
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.extension.merge
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider


@Service
class MarksServiceImpl(val pointsProviders: List<RateMapPointProvider>): MarksService {
    override fun saveMark(mark: Double, lat: Double, lng: Double, radius: Long) {
        pointsProviders
            .filter { it.getSearchDistance() >= radius }
            .sortedBy { it.getSearchDistance() }
            .forEachIndexed { idx, provider -> provider.findNear(lng, lat)
                .take(if (idx == 0) 3 else 1)
                .forEach { point ->  provider.saveOrUpdate(point.also {
                    it.rates.mark.merge(AvgValue(mark, 1))
                }) }
            }
    }

    override fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult = (pointsProviders
        .sortedByDescending { it.getSearchDistance() }
        .firstOrNull { it.getSearchDistance() <= radius } ?: pointsProviders.last())
        .let { MarksResult(it.findNear(lng, lat), it.getSearchDistance()) }

    override fun saveAvgMeterPrice(point: GeoPoint, value: AvgValue) {
        pointsProviders.map {
            it.findNear(point.lng, point.lat)
                .firstOrNull()
                ?.also { point -> point.rates.avgMeterPrice.merge(value) }
                ?.also { point -> it.saveOrUpdate(point) }
        }
    }
}
