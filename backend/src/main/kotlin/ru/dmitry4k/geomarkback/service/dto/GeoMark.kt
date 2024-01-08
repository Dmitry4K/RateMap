package ru.dmitry4k.geomarkback.service.dto

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

data class GeoMark(
    val id: String? = null,
    val geoPoint: GeoPoint,
    var mark: Double,
    var count: Long
) {
    fun toDao() = GeoPointDao(
        id = id,
        mark = mark,
        count = count,
        point = GeoJsonPoint(geoPoint.lng, geoPoint.lat)
    )

    companion object {
        fun valueOf(dao: GeoPointDao) =
            GeoMark(
                id = dao.id,
                geoPoint = GeoPoint(dao.point!!.y, dao.point!!.x),
                mark = dao.mark!!,
                count = dao.count!!
            )
    }
}