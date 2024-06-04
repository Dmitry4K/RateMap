package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.layer.RateMapLayer
import com.yandex.mapkit.geometry.geo.Projection
import com.yandex.mapkit.images.ImageUrlProvider
import com.yandex.mapkit.layers.LayerOptions
import com.yandex.mapkit.tiles.UrlProvider

abstract class AbstractRateMapLayer(
    private val name: String,
    private val format: String,
    private val urlProvider: UrlProvider,
    private val imageUrlProvider: ImageUrlProvider,
    private val projectionConsumer: () -> Projection
): RateMapLayer {
    private lateinit var projection: Projection
    private val options: LayerOptions = LayerOptions().apply {
        this.animateOnActivation = false
        this.tileAppearingAnimationDuration = 10L
    }
    override fun init() {
        projection = projectionConsumer()
    }
    override fun getOptions(): LayerOptions = options

    override fun getName(): String = name

    override fun getFormat(): String = format

    override fun getUrlProvider(): UrlProvider = urlProvider

    override fun getImageUrlProvider(): ImageUrlProvider = imageUrlProvider
    override fun getProjection(): Projection = projection
}