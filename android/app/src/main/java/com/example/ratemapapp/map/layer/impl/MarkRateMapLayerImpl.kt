package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.provider.AvgMarkUrlProviderImpl
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.yandex.mapkit.geometry.geo.Projections

class MarkRateMapLayerImpl: AbstractRateMapLayer(
    "marks",
    "image/png",
    AvgMarkUrlProviderImpl(),
    ImageUrlProviderImpl(),
    { Projections.getWgs84Mercator() }
)
