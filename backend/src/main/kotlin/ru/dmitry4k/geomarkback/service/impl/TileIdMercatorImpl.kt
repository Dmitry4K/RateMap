package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.service.Mercator
import ru.dmitry4k.geomarkback.service.TileIdMercator
import ru.dmitry4k.geomarkback.service.dto.GeoPoint
import ru.dmitry4k.geomarkback.service.dto.TileId
import ru.dmitry4k.geomarkback.service.dto.XYPoint

@Component
class TileIdMercatorImpl(
    private val mercator: Mercator
) : TileIdMercator {
    private val top = 23810769.32
    private val bottom = -23810769.32
    private val right = 20037508.34
    private val left = -20037508.34
    private val yAxis = top-bottom
    private val xAxis = right-left

    override fun getPointByTileId(tileId: TileId): GeoPoint {
        val xy = XYPoint(
            normalize(tileId.x, tileId.z.toDouble(), xAxis) - xAxis / 2.0,
            normalize(tileId.y, tileId.z.toDouble(), yAxis) - yAxis / 2.0
        )
        return mercator.xyToPoint(xy)
    }

    private fun normalize(v: Double, origSize: Double, newSize: Double): Double {
        return (v  / origSize) * newSize
    }

    override fun getTileIdByPoint(geoPoint: GeoPoint, z: Int): TileId {
        val xy = mercator.pointToXY(geoPoint)
        return TileId(
            normalize(xy.x + xAxis / 2.0, xAxis, z.toDouble()),
            normalize(xy.y + yAxis / 2.0, yAxis, z.toDouble()),
            z
        )
    }
}