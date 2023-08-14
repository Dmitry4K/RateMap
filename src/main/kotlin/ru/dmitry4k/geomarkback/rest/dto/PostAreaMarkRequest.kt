package ru.dmitry4k.geomarkback.rest.dto

data class PostAreaMarkRequest(
    val rate: Float,
    val points: List<GeoPointRequest>
)