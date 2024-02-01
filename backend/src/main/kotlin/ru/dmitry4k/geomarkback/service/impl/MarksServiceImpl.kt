package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider
import java.util.logging.Logger


@Service
class MarksServiceImpl(pointsProviders: List<RateMapPointProvider>): MarksService {
    private val log = Logger.getLogger(MarksServiceImpl::class.java.name)

    val sortedPointsServices: List<RateMapPointProvider> = pointsProviders.sortedBy { it.getSearchDistance() }

    override fun saveMark(mark: Double, lat: Double, lng: Double, depth: Long) {
        //log.info("Saving rate with mark: $mark; lat: $lat; lng: $lng; depth: $depth;")
        sortedPointsServices.filter { it.getSearchDistance() >= depth }
            .forEach { it.findNear(lng, lat)
                .take(3)
                .forEach { point ->
                    point.mark = calculateNewMark(point.count!!, point.mark!!, mark)
                    point.count = point.count!! + 1
                    it.saveOrUpdate(point)
                }
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

    private fun calculateNewMark(oldCount: Long, oldMark: Double, newMark: Double): Double {
        val newCount = oldCount + 1
        return if (oldCount == 0L) newMark else oldMark * (oldCount.toDouble() / newCount.toDouble()) + newMark / newCount.toDouble()
    }
}