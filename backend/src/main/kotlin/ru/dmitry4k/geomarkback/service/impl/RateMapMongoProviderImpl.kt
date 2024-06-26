package ru.dmitry4k.geomarkback.service.impl

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.service.RateMapPointProvider

private const val NEAR_QUERY = "{ \"point.coordinates\": { \$near: { \$geometry: { type: \"Point\", coordinates: [ %s, %s]}, \$maxDistance: %d, \$minDistance: %d }}}"
private const val NEAR_QUERY_BY_POLYGON = "{\n" +
        "  \"point.coordinates\": {\n" +
        "    \$geoWithin : {\n" +
        "        \$geometry: {\n" +
        "          type: \"Polygon\",\n" +
        "          coordinates: [ [ %s ] ]\n" +
        "        }\n" +
        "      }\n" +
        "  }\n" +
        "}\n"
class RateMapMongoProviderImpl(
    private val area: String,
    private val tableName: String,
    private val distance: Long,
    private val mongoTemplate: MongoTemplate
): RateMapPointProvider {
    override fun getArea() = area
    override fun getAverageDistanceBetweenPoints() = distance
    override fun findNearsOrClosest(lng: Double, lat: Double, radius: Long): List<GeoPointDao> {
        val query = BasicQuery(
            NEAR_QUERY.format(
                lng.toString().replace(',', '.'),
                lat.toString().replace(',', '.'),
                ((if (radius < getAverageDistanceBetweenPoints()) getAverageDistanceBetweenPoints() else radius) * 1.2).toLong(),
                0
            )
        )
        return mongoTemplate.find(query, GeoPointDao::class.java, tableName)
    }

    override fun findByPolygon(polygon: List<GeoPoint>): List<GeoPointDao> {
        val formattedPolygon = polygon.toMutableList()
            .also { it.add(polygon.first()) }
            .joinToString(", ") { "[${it.lng}, ${it.lat}]" }
        val query = BasicQuery(NEAR_QUERY_BY_POLYGON.format(formattedPolygon))
        return mongoTemplate.find(query, GeoPointDao::class.java, tableName)
    }

    override fun saveOrUpdate(point: GeoPointDao) {
        mongoTemplate.save(point, tableName)
    }
}