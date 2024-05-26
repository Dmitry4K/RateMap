package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.dto.GeoPoint

interface RateMapPointProvider {
    fun getArea(): String
    fun getAverageDistanceBetweenPoints(): Long
    fun findNearsOrClosest(lng: Double, lat: Double, radius: Long): List<GeoPointDao>
    fun findByPolygon(polygon: List<GeoPoint>): List<GeoPointDao>
    fun saveOrUpdate(point: GeoPointDao)
}