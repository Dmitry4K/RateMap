package com.example.ratemapapp.map.provider

import android.util.Log
import com.yandex.mapkit.TileId
import com.yandex.mapkit.Version
import com.yandex.mapkit.geometry.geo.Projections
import com.yandex.mapkit.geometry.geo.XYPoint
import com.yandex.mapkit.tiles.UrlProvider

class RateMapUrlProviderImpl : UrlProvider {
    override fun formatUrl(tileId: TileId, version: Version): String {
        val projection = Projections.getWgs84Mercator()
        val point = projection.xyToWorld(XYPoint(tileId.x.toDouble() + 0.5, tileId.y.toDouble() + 0.5), tileId.z)
        Log.i("URL", "lat: ${point.latitude}, lng: ${point.longitude}, x: ${tileId.x}, y: ${tileId.y}, z: ${tileId.z}")
        return "http://10.0.2.2:8080/api/tile/yandex/png?x=${tileId.x}&y=${tileId.y}&z=${tileId.z}"
    }
}
