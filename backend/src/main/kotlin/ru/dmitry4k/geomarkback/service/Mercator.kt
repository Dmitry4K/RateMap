package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.XYPoint

interface Mercator {
    fun pointToXY(geoPoint: GeoPoint): XYPoint
    fun xyToPoint(xyPoint: XYPoint): GeoPoint

    fun top(): Double
    fun bottom(): Double
    fun left(): Double
    fun right(): Double
}