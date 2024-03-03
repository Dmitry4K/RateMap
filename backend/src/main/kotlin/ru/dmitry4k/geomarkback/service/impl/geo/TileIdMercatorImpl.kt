package ru.dmitry4k.geomarkback.service.impl.geo

import org.springframework.stereotype.Component
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.TileId
import ru.dmitry4k.geomarkback.dto.Point2D
import ru.dmitry4k.geomarkback.service.geo.Mercator
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator

@Component
class TileIdMercatorImpl(
    private val mercator: Mercator
) : TileIdMercator {
    private val top = mercator.top()
    private val bottom = mercator.bottom()
    private val right = mercator.right()
    private val left = mercator.left()
    private val yAxis = top-bottom
    private val xAxis = right-left

    override fun getPointByTileId(tileId: TileId): GeoPoint {
        val tilesCount = getTileCount(tileId.z)
        val xy = Point2D(
            normalize(tileId.x, tilesCount.toDouble(), xAxis) - xAxis / 2.0,
            normalize(tilesCount - tileId.y, tilesCount.toDouble(), yAxis) - yAxis / 2.0
        )
        return mercator.xyToPoint(xy)
    }

    private fun normalize(v: Double, origSize: Double, newSize: Double): Double {
        return (v  / origSize) * newSize
    }

    override fun getTileIdByPoint(geoPoint: GeoPoint, z: Int): TileId {
        val xy = mercator.pointToXY(geoPoint)
        val tilesCount = getTileCount(z)
        return TileId(
            normalize(xy.x + xAxis / 2.0, xAxis, tilesCount.toDouble()),
            normalize(yAxis / 2.0 - xy.y, yAxis, tilesCount.toDouble()),
            z
        )
    }

    private fun getTileCount(z: Int): Int {
        return 1 shl z
    }
}