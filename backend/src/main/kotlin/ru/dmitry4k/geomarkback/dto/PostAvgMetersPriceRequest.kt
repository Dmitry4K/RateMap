package ru.dmitry4k.geomarkback.dto

data class PostAvgMetersPriceRequest(
    val point: GeoPoint,
    val avgMeterPrice: AvgValue
)
