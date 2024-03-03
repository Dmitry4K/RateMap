package ru.dmitry4k.geomarkback.service

import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.Point2D

abstract class AbstractMercator(
    private val top: Double,
    private val bottom: Double,
    private val left: Double,
    private val right: Double
) : Mercator {
    override fun pointToXY(geoPoint: GeoPoint)= with(geoPoint) {
        Point2D(xAxisProjection(lng), yAxisProjection(lat))
    }

    override fun xyToPoint(point2D: Point2D<Double, Double>) = with(point2D) {
        GeoPoint(latAxisProjection(y), lngAxisProjection(x))
    }


    override fun top(): Double = top

    override fun bottom(): Double = bottom

    override fun left(): Double = left

    override fun right(): Double = right

    protected abstract fun xAxisProjection(lng: Double): Double

    protected abstract fun yAxisProjection(lat: Double): Double

    protected abstract fun latAxisProjection(y: Double): Double

    protected abstract fun lngAxisProjection(x: Double): Double
}