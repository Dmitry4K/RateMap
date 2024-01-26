package ru.dmitry4k.geomarkback.service

interface GeoMathService {
    fun mercator(): Mercator
    fun distance(): Distance
}