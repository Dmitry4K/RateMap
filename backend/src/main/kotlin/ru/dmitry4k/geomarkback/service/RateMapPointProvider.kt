package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

interface RateMapPointProvider {
    fun getArea(): String
    fun getSearchDistance(): Long
    fun findNear(lng: Double, lat: Double): List<GeoPointDao>

    fun saveOrUpdate(point: GeoPointDao)
}