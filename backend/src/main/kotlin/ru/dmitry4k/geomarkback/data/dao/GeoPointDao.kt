package ru.dmitry4k.geomarkback.data.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class GeoPointDao(
    @Id
    var id: String? = null,
    var mark: Double? = null,
    var count: Long? = null,
    var point: GeoJsonPoint? = null,
)
