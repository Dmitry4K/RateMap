package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.PointsService


@Service
class MarksServiceImpl(
    val pointsService: PointsService
) : MarksService {
    override fun saveMark(mark: Double, lat: Double, lng: Double) {
        val point = pointsService.nearestPoint(lat, lng)
        val newMark = with(point) {
            this.mark * (count.toDouble() / (count + 1).toDouble())
        }
        val newCount = point.count + 1
        point.mark = newMark
        point.count = newCount
        pointsService.saveOrUpdate(point)
    }
}