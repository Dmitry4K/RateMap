package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.AvgValue
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.extension.merge
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider
import ru.dmitry4k.geomarkback.service.geo.Distance


@Service
class MarksServiceImpl(
    val pointsProviders: List<RateMapPointProvider>,
    val distance: Distance
): MarksService {
    override fun saveMark(mark: Double, polygon: List<GeoPoint>) {
        val centerPoint = GeoPoint(
            lat = polygon.map { it.lat }.average(),
            lng = polygon.map { it.lng }.average()
        )
        val averageDistanceBetweenPoints = polygon.subList(0, polygon.size - 1)
            .mapIndexed { idx, point -> distance.distance(point, polygon[idx+1])}
            .average()
        pointsProviders
            .filter { it.getAverageDistanceBetweenPoints() >= averageDistanceBetweenPoints / 10 }
            .sortedBy { it.getAverageDistanceBetweenPoints() }
            .forEach { provider ->
                provider.findByPolygon(polygon).ifEmpty {
                    provider
                        .findNearsOrClosest(centerPoint.lng, centerPoint.lat, provider.getAverageDistanceBetweenPoints())
                        .subList(0, 1)
                }
                .forEach { point ->  provider.saveOrUpdate(point.also {
                    it.rates.mark.merge(AvgValue(mark, 1))
                }) }
            }
    }

    override fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult = (pointsProviders
        .sortedByDescending { it.getAverageDistanceBetweenPoints() }
        .firstOrNull { it.getAverageDistanceBetweenPoints() <= radius / 5 } ?: pointsProviders.last())
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
