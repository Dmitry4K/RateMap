package ru.dmitry4k.geomarkback.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cache")
data class CacheProperties(
    val connection: Connection,
    val collections: Map<String, Cache>
)

data class Cache(
    val ttl: Long
)

data class Connection(
    val host: String,
    val pass: String,
    val port: Int
)