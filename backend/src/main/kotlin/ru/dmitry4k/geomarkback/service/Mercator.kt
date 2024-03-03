package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.Point2D

interface Mercator {
    fun pointToXY(geoPoint: GeoPoint): Point2D<Double, Double>
    fun xyToPoint(point2D: Point2D<Double, Double>): GeoPoint

    fun top(): Double
    fun bottom(): Double
    fun left(): Double
    fun right(): Double
}