package com.example.ratemapapp.components.impl

import com.example.ratemapapp.components.PlaceMark
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.Map

class PlaceMarkImpl(
    private val map: Map
) : PlaceMark {
    private lateinit var placemarkMapObject: PlacemarkMapObject
    override fun remove() {
        map.mapObjects.remove(placemarkMapObject)
    }

    override fun place(point: Point): Point {
        placemarkMapObject = map.mapObjects.addPlacemark(point)
        return placemarkMapObject.geometry
    }

    override fun point(): Point {
        return placemarkMapObject.geometry
    }
}