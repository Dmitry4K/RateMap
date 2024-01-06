package com.example.ratemapapp.map.provider

import com.yandex.mapkit.TileId
import com.yandex.mapkit.Version
import com.yandex.mapkit.tiles.UrlProvider

class RateMapUrlProviderImpl : UrlProvider {
    override fun formatUrl(tileId: TileId, version: Version): String {
        return "https://ratemap.ru/api/tile/yandex/png?x=${tileId.x}&y=${tileId.y}&z=${tileId.z}"
    }
}
