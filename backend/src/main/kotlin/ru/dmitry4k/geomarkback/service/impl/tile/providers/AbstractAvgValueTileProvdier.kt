package ru.dmitry4k.geomarkback.service.impl.tile.providers

import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.Point3D
import ru.dmitry4k.geomarkback.dto.TileId
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.geo.Distance
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator
import ru.dmitry4k.geomarkback.service.tile.LegendRenderer
import ru.dmitry4k.geomarkback.service.tile.TileRenderer
import ru.dmitry4k.geomarkback.service.tile.TileSettingsProvider
import ru.dmitry4k.geomarkback.service.tile.YandexTileProvider
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min

abstract class AbstractAvgValueTileProvdier(
    private val tileIdMercator: TileIdMercator,
    private val distance: Distance,
    private val markService: MarksService,
    private val tileRenderer: TileRenderer,
    private val legendRenderer: LegendRenderer,
    private val tileSettingsProvider: TileSettingsProvider
): YandexTileProvider {
    override fun getTile(x: Int, y: Int, z: Int): ByteArray {
        val center = tileIdMercator.getPointByTileId(TileId(x + 0.5, y + 0.5, z))
        //println("lat: ${center.lat}, lng: ${center.lng}")

        val maxDistance = listOf(
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y + 1.0, z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y + 1.0, z))
        ).maxOf { distance.distance(it, center) }

        val searchDistance = maxDistance * 2.0
        val marksResult = markService.getMarks(center.lat, center.lng, searchDistance.toLong())

        val tileSize = tileRenderer.getTileSize()
        val points = marksResult.points.map {
            val geoPoint = GeoPoint(it.point.y, it.point.x)
            val tileId = tileIdMercator.getTileIdByPoint(geoPoint, z)
            Point3D(
                (tileSize * (tileId.x - x.toDouble())).toInt(),
                (tileSize * (tileId.y - y.toDouble())).toInt(),
                norm(it)
            )
        }
        val radius = marksResult.distance.toDouble() * tileSize.toDouble() / maxDistance / cos(45.0) / 1.5

        return tileRenderer.renderTile(points, radius.toInt())
    }

    override fun getLegend(width: Int, height: Int, fontSize: Int): ByteArray {
        val gradient = listOf(
            getMinValue() to tileSettingsProvider.getLowestColor(),
            getMaxValue() to tileSettingsProvider.getHighestColor()
        )
        return legendRenderer.renderLegend(width, height, gradient, fontSize)
    }

    private fun norm(point: GeoPointDao): Double {
        return max(0.0, min(1.0, (getValue(point) - getMinValue()) / (getMaxValue() - getMinValue())))
    }

    abstract fun getValue(point: GeoPointDao): Double

    abstract fun getMaxValue(): Double

    abstract fun getMinValue(): Double
}
