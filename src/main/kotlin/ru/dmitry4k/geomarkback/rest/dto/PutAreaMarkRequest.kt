package ru.dmitry4k.geomarkback.rest.dto

data class PutAreaMarkRequest(
    val points: List<GeoPointRequest>,
    val mark: Int
)