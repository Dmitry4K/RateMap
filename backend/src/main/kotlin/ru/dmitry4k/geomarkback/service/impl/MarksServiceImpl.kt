package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.RateMapPointProvider
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.service.MarksService


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

    override fun getMarks(lat: Double, lng: Double, depth: Long): List<GeoPointDao> {
        var selectedPointService = sortedPointsServices.first()
        for (pointsService in sortedPointsServices) {
            if (depth < pointsService.getSearchDistance()) break
            selectedPointService = pointsService
        }
        return selectedPointService.findNear(lng, lat)
    }
}