package com.example.ratemapapp.dto

data class PostMarkRequestBody(
    val lat: Double,
    val lng: Double,
    val mark: Double,
    val depth: Long
)