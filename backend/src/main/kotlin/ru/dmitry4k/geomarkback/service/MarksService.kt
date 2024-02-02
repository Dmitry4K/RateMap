package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.MarksResult

interface MarksService {
    fun saveMark(mark: Double, lat: Double, lng: Double, radius: Long)

    fun getMarks(lat: Double, lng: Double, radius: Long): MarksResult
}