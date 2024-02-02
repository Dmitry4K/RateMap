package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider
import java.util.logging.Logger


@Service
class MarksServiceImpl(val pointsProviders: List<RateMapPointProvider>): MarksService {
    private val log = Logger.getLogger(MarksServiceImpl::class.java.name)

    override fun saveMark(mark: Double, lat: Double, lng: Double, radius: Long) {
        //log.info("Saving rate with mark: $mark; lat: $lat; lng: $lng; depth: $depth;")
        pointsProviders.filter { it.getSearchDistance() >= radius }
            .sortedBy { it.getSearchDistance() }
            .forEachIndexed { idx, provider -> provider.findNear(lng, lat)
                .take(if (idx == 0) 3 else 1)
                .forEach { point ->
                    point.mark = calculateNewMark(point.count!!, point.mark!!, mark)
                    point.count = point.count!! + 1
                    provider.saveOrUpdate(point)
                }
            }
    }

    override fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult {
        val sortedPointsServices = pointsProviders.sortedBy { it.getSearchDistance() }
        var selectedPointService = sortedPointsServices.first()
        for (pointsService in sortedPointsServices) {
            if (radius < pointsService.getSearchDistance()) break
            selectedPointService = pointsService
        }
        return MarksResult(selectedPointService.findNear(lng, lat), selectedPointService.getSearchDistance())
    }

    private fun calculateNewMark(oldCount: Long, oldMark: Double, newMark: Double): Double {
        val newCount = oldCount + 1
        return if (oldCount == 0L) newMark else oldMark * (oldCount.toDouble() / newCount.toDouble()) + newMark / newCount.toDouble()
    }
}