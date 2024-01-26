package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.GeoPoint

interface Distance {
    fun distance(a: GeoPoint, b: GeoPoint): Long
}