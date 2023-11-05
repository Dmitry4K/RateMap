package ru.dmitry4k.geomarkback.service

interface MarksService {
    fun saveMark(mark: Double, lat: Double, lng: Double, depth: Long)
}