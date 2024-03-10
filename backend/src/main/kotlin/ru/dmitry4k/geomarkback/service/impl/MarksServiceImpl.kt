package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.AvgValue
import ru.dmitry4k.geomarkback.dto.MarksResult
import ru.dmitry4k.geomarkback.extension.merge
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.RateMapPointProvider
import java.util.logging.Logger


@Service
class MarksServiceImpl(val pointsProviders: List<RateMapPointProvider>): MarksService {
    private val log = Logger.getLogger(MarksServiceImpl::class.java.name)

    override fun saveMark(mark: Double, lat: Double, lng: Double, radius: Long) {
        //log.info("Saving rate with mark: $mark; lat: $lat; lng: $lng; depth: $depth;")
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
}
