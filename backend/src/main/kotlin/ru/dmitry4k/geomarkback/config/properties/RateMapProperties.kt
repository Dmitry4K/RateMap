package ru.dmitry4k.geomarkback.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "ratemap")
data class RateMapProperties(
    val connectionString: String,
    val layers: Map<String, RegionProperty>,
    val areas: Map<String, AreaProperty>
)

data class RegionProperty(
    val searchDistance: Long
)

data class AreaProperty(
    val center: AreaCenter,
    val radius: Double
)

data class AreaCenter(
    val lat: Double,
    val lng: Double
)