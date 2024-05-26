package com.example.ratemapapp.dto

data class PostMarkRequestBody(
    val polygon: List<GeoPoint>,
    val mark: Double
)

data class GeoPoint(
    val lat: Double,
    val lng: Double
)