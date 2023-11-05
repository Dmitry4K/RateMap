package ru.dmitry4k.geomarkback.rest.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PostMarkRequestBody(
    val lat: Double,
    val lng: Double,
    @Min(1)
    @Max(5)
    val mark: Double,
    val depth: Long
)