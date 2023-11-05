package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.PointsService


@Service
class MarksServiceImpl(pointsServices: List<PointsService>): MarksService {
    val sortedPointsServices: List<PointsService> = pointsServices.sortedBy { it.getMapDepth() }

    override fun saveMark(mark: Double, lat: Double, lng: Double, depth: Long) {
        for (pointsService in sortedPointsServices) {
            val point = pointsService.nearestPoint(lat, lng)
            val newMark = with(point) {
                this.mark * (count.toDouble() / (count + 1).toDouble())
            }
            val newCount = point.count + 1
            point.mark = newMark
            point.count = newCount
            pointsService.saveOrUpdate(point)
            if (depth < pointsService.getMapDepth()) break
        }
    }
}