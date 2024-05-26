package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.AvgValue
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.MarksResult

interface MarksService {
    fun saveMark(mark: Double, polygon: List<GeoPoint>)

    fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult

    fun saveAvgMeterPrice(point: GeoPoint, value: AvgValue)
}