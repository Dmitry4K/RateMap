package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao

interface MarksService {
    fun saveMark(mark: Double, lat: Double, lng: Double, depth: Long)

    fun getMarks(lat: Double, lng: Double, depth: Long): List<GeoPointDao>
}