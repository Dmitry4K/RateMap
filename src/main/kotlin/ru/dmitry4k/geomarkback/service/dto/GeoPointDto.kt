package ru.dmitry4k.geomarkback.service.dto

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

data class GeoPointDto(
    val id: String? = null,
    val lat: Double,
    val lng: Double,
    var mark: Double,
    var count: Long
) {
    fun toDao() = GeoPointDao(
        id = id,
        mark = mark,
        count = count,
        point = GeoJsonPoint(lng, lat)
    )

    companion object {
        fun valueOf(dao: GeoPointDao) =
            GeoPointDto(
                id = dao.id,
                lat = dao.point!!.y,
                lng = dao.point!!.x,
                mark = dao.mark!!,
                count = dao.count!!
            )
    }
}