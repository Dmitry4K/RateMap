package ru.dmitry4k.geomarkback.rest.dto

data class PostMarkRequestBody(
    val lat: Double,
    val lng: Double,
    val rate: Float
)