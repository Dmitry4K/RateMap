package com.example.ratemapapp.map.provider

import com.yandex.mapkit.TileId
import com.yandex.mapkit.Version
import com.yandex.mapkit.tiles.UrlProvider

class RateMapUrlProviderImpl : UrlProvider {
    override fun formatUrl(tileId: TileId, version: Version): String {
        return "http://10.0.2.2:8080/tile/yandex/png?x=${tileId.x}&y=${tileId.y}&z=${tileId.z}"
    }
}
