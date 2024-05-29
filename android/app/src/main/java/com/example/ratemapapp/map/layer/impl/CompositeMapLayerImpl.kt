package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.provider.AvgMeterPriceUrlProviderImpl
import com.example.ratemapapp.map.provider.CompositeUrlProviderImpl
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.yandex.mapkit.geometry.geo.Projections
import com.yandex.mapkit.layers.LayerOptions

class CompositeMapLayerImpl: AbstractRateMapLayer(
    "composite",
    "image/png",
    LayerOptions(),
    CompositeUrlProviderImpl(),
    ImageUrlProviderImpl(),
    { Projections.getWgs84Mercator() }
)
