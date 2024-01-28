package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider


@Service
class MarksServiceImpl(pointsProviders: List<RateMapPointProvider>): MarksService {
    val sortedPointsServices: List<RateMapPointProvider> = pointsProviders.sortedBy { it.getSearchDistance() }

    override fun saveMark(mark: Double, lat: Double, lng: Double, depth: Long) {
        for (pointsService in sortedPointsServices) {
            val point = pointsService.findNear(lat, lng).first()
            val newMark = with(point) {
                this.mark!! * (count!!.toDouble() / (count!! + 1).toDouble())
            }
            val newCount = point.count!! + 1
            point.mark = newMark
            point.count = newCount
            pointsService.saveOrUpdate(point)
            if (depth < pointsService.getSearchDistance()) break
        }
    }

    override fun getMarks(lat: Double, lng: Double, depth: Long): MarksResult {
        var selectedPointService = sortedPointsServices.first()
        for (pointsService in sortedPointsServices) {
            if (depth < pointsService.getSearchDistance()) break
            selectedPointService = pointsService
        }
        return MarksResult(selectedPointService.findNear(lng, lat), selectedPointService.getSearchDistance())
    }
}