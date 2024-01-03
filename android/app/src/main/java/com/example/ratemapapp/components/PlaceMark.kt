package com.example.ratemapapp.components

import com.yandex.mapkit.geometry.Point

interface PlaceMark {
    fun remove()
    fun place(point: Point): Point
    fun point(): Point
}