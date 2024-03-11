package com.example.ratemapapp.map.layer

import com.yandex.mapkit.geometry.geo.Projection
import com.yandex.mapkit.images.ImageUrlProvider
import com.yandex.mapkit.layers.LayerOptions
import com.yandex.mapkit.tiles.UrlProvider

interface RateMapLayer {
    fun init()
    fun getOptions(): LayerOptions
    fun getName(): String
    fun getFormat(): String
    fun getUrlProvider(): UrlProvider
    fun getImageUrlProvider(): ImageUrlProvider
    fun getProjection(): Projection
}
