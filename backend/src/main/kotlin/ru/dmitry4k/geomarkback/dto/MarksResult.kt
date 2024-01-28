package ru.dmitry4k.geomarkback.dto

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

data class MarksResult(
    val points: List<GeoPointDao>,
    val distance: Long
)