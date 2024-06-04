package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.provider.CompositeUrlProviderImpl
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.yandex.mapkit.geometry.geo.Projections

class CompositeMapLayerImpl: AbstractRateMapLayer(
    "composite",
    "image/png",
    CompositeUrlProviderImpl(),
    ImageUrlProviderImpl(),
    { Projections.getWgs84Mercator() }
)
