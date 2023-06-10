package com.example.GeoMarkBack.rest.dto

data class PostMarkRequestBody(
    val lat: Double,
    val long: Double,
    val rate: Double
)