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
            .filter { it.getAverageDistanceBetweenPoints() >= radius }
            .sortedBy { it.getAverageDistanceBetweenPoints() }
            .forEachIndexed { idx, provider -> provider.findNearsOrClosest(lng, lat, radius)
                .take(if (idx == 0) 3 else 1)
                .forEach { point ->  provider.saveOrUpdate(point.also {
                    it.rates.mark.merge(AvgValue(mark, 1))
                }) }
            }
    }

    override fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult = (pointsProviders
        .sortedByDescending { it.getAverageDistanceBetweenPoints() }
        .firstOrNull { it.getAverageDistanceBetweenPoints() <= radius / 2.5 } ?: pointsProviders.last())
        .let { MarksResult(it.findNearsOrClosest(lng, lat, radius), it.getAverageDistanceBetweenPoints()) }

    override fun saveAvgMeterPrice(point: GeoPoint, value: AvgValue) {
        pointsProviders.map {
            it.findNearsOrClosest(point.lng, point.lat, it.getAverageDistanceBetweenPoints())
                .firstOrNull()
                ?.also { point -> point.rates.avgMeterPrice.merge(value) }
                ?.also { point -> it.saveOrUpdate(point) }
        }
    }
}
