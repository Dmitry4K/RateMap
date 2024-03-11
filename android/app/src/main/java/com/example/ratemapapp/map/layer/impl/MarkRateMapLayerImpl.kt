package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.provider.AvgMarkUrlProviderImpl
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.yandex.mapkit.geometry.geo.Projections
import com.yandex.mapkit.layers.LayerOptions

class MarkRateMapLayerImpl: AbstractRateMapLayer(
    "marks",
    "image/png",
    LayerOptions(),
    AvgMarkUrlProviderImpl(),
    ImageUrlProviderImpl(),
    { Projections.getWgs84Mercator() }
)
