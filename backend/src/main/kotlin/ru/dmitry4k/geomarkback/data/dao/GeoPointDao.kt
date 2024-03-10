package ru.dmitry4k.geomarkback.data.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import ru.dmitry4k.geomarkback.dto.Rates

@Document
data class GeoPointDao(
    @Id
    var id: String? = null,
    val rates: Rates,
    val point: GeoJsonPoint,
)
