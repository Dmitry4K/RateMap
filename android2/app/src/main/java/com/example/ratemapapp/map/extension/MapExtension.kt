package com.example.ratemapapp.map.extension

import com.example.ratemapapp.map.layer.RateMapLayer
import com.yandex.mapkit.layers.Layer

fun com.yandex.mapkit.map.Map.setLayer(rateMapLayer: RateMapLayer): Layer = addLayer(
    rateMapLayer.getName(),
    rateMapLayer.getFormat(),
    rateMapLayer.getOptions(),
    rateMapLayer.getUrlProvider(),
    rateMapLayer.getImageUrlProvider(),
    rateMapLayer.getProjection()
)
