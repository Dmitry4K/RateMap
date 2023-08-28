package ru.dmitry4k.geomarkback.rest.dto

data class PostAreaMarkRequest(
    val rate: Double,
    val points: List<GeoPointRequest>
)