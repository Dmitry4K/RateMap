package com.example.ratemapapp.fragment

import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.*

class RatingPolygon(
    private val mapObjects: MapObjectCollection
) {
    private val points: MutableList<Point> = mutableListOf()
    private val placemarkMapObjects: MutableList<PlacemarkMapObject> = mutableListOf()
    private var polygon: PolygonMapObject? = null

    fun addPoint(point: Point) {
        addInternalPoint(point)
        recreateInternalPolygon()
    }

    fun removePolygon() {
        removeInternalPolygon()
        clearInternalPoints()
    }

    fun getAveragePoint() = Point(
        points.sumOf { it.latitude } / points.size,
        points.sumOf { it.longitude } / points.size
    )

    fun getPoints(): List<Point> = points

    private fun addInternalPoint(point: Point) {
        points.add(point)
        placemarkMapObjects.add(createPlaceMark(point, points.size - 1))
    }

    private fun createPlaceMark(point: Point, index: Int): PlacemarkMapObject {
        return mapObjects.addPlacemark(point).apply {
            isDraggable = true
            setDragListener(object : MapObjectDragListener {
                override fun onMapObjectDragStart(p0: MapObject) {}
                override fun onMapObjectDragEnd(p0: MapObject) {}
                override fun onMapObjectDrag(p0: MapObject, p1: Point) {
                    points.removeAt(index)
                    points.add(index, p1)
                    recreateInternalPolygon()
                }
            })
        }
    }

    private fun removeInternalPolygon() {
        if (polygon != null) {
            mapObjects.remove(polygon!!)
            polygon = null
        }
    }

    private fun clearInternalPoints() {
        placemarkMapObjects.forEach { mapObjects.remove(it) }
        placemarkMapObjects.clear()
        points.clear()
    }

    private fun createInternalPolygon() {
        polygon = Polygon(LinearRing(points.map { it }), listOf()).let { mapObjects.addPolygon(it) }
    }

    private fun recreateInternalPolygon() {
        removeInternalPolygon()
        createInternalPolygon()
    }
}