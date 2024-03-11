package com.example.ratemapapp.map.layer.impl

import com.example.ratemapapp.map.provider.AvgMeterPriceUrlProviderImpl
import com.example.ratemapapp.map.provider.ImageUrlProviderImpl
import com.yandex.mapkit.geometry.geo.Projections
import com.yandex.mapkit.layers.LayerOptions

class AvgMeterPriceMapLayerImpl: AbstractRateMapLayer(
    "avgMetersPrice",
    "image/png",
    LayerOptions(),
    AvgMeterPriceUrlProviderImpl(),
    ImageUrlProviderImpl(),
    { Projections.getWgs84Mercator() }
)
