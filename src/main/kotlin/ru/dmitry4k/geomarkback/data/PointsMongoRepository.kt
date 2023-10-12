package ru.dmitry4k.geomarkback.data

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

private const val NEAR_QUERY = "{ \"point.coordinates\": { \$near: { \$geometry: { type: \"Point\", coordinates: [ ?0, ?1]}, \$maxDistance: ?2, \$minDistance: ?3 }}}"

interface PointsMongoRepository : MongoRepository<GeoPointDao, String> {
    @Query(NEAR_QUERY)
    fun findNear(lng: Double, lat: Double, maxDistance: Long, minDistance: Long): List<GeoPointDao>
}