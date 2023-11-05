package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.PointsMongoRepository
import ru.dmitry4k.geomarkback.service.PointsService
import ru.dmitry4k.geomarkback.service.dto.GeoPointDto


private const val distance = 1000L

@Service
class PointsServiceImpl(
    val pointsMongoRepository: PointsMongoRepository,
    val depth: Long = distance
): PointsService {
    override fun nearestPoint(lat: Double, lng: Double) = GeoPointDto.valueOf(
        pointsMongoRepository
            .findNear(lng, lat, depth, 0)
            .first()
    )

    override fun saveOrUpdate(point: GeoPointDto) {
        pointsMongoRepository.save(point.toDao())
    }

    override fun getMapDepth(): Long {
        return depth
    }
}