package ru.dmitry4k.geomarkback.services.impl

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.math.MathEngine
import ru.dmitry4k.geomarkback.math.MathEngine.Companion.getCircleAreaIntersection
import ru.dmitry4k.geomarkback.math.MathEngine.Companion.getDistanceBetweenTwoPointsInMeters
import ru.dmitry4k.geomarkback.services.PointRateService
import ru.dmitry4k.geomarkback.services.PointsService
import ru.dmitry4k.geomarkback.services.dto.GeoPoint
import ru.dmitry4k.geomarkback.services.dto.GeoPointRate
import ru.dmitry4k.geomarkback.services.dto.GeoRate
import kotlin.math.pow

private const val EXP = 4
private const val NEIGHBORS_RADIUS_RATIO = 1.95

@Component
class PointRateServiceImpl(
//    val pointsService: PointsService
) : PointRateService {
    override fun addRate(rate: Double, point: GeoPoint): GeoPointRate {
        TODO()
        // 1 Добавить инициализацию нового rate'а
        // 2 Добавить инициализацию нового веса точки
        //
//        val neighbors = pointsService.getPoints(point, point.radius * NEIGHBORS_RADIUS_RATIO)
//        val weight = neighbors.asSequence()
//            .map { it.rate.weight * getCircleAreaIntersection(it.point, point) }
//            .sum() / neighbors.size
//        val newRate = neighbors.asSequence()
//            .map { it.rate.rate * it.rate.weight.pow(EXP) }
//            .sum()
//            .plus(rate * weight.pow(EXP))
//            .div(neighbors.asSequence()
//                .map { it.rate.weight.pow(EXP) }
//                .sum()
//                .plus(weight.pow(EXP))
//            )

        return GeoPointRate(GeoRate(1.0, 1.0), point)
    }
}