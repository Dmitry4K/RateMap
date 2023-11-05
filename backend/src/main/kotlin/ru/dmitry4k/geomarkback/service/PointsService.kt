package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.service.dto.GeoPointDto

interface PointsService : RateMapSingleTableService {
    fun nearestPoint(lat: Double, lng: Double): GeoPointDto
    fun saveOrUpdate(point: GeoPointDto)
}