package ru.dmitry4k.geomarkback.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PostMarkRequestBody(
    val polygon: List<GeoPoint>,
    @Min(1)
    @Max(5)
    val mark: Double,
    val radius: Long
)