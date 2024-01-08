package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.service.dto.GeoPoint
import ru.dmitry4k.geomarkback.service.dto.XYPoint

interface Mercator {
    fun pointToXY(geoPoint: GeoPoint): XYPoint
    fun xyToPoint(xyPoint: XYPoint): GeoPoint
}