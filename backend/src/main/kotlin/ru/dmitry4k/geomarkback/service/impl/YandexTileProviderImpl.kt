package ru.dmitry4k.geomarkback.service.impl

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.dto.GeoPoint
import ru.dmitry4k.geomarkback.dto.TileId
import ru.dmitry4k.geomarkback.service.*
import java.util.logging.Logger
import kotlin.math.cos

val log: Logger = Logger.getLogger("YandexTileServiceImpl")

@Service
class YandexTileProviderImpl(
    val tileIdMercator: TileIdMercator,
    val distance: Distance,
    val markService: MarksService,
) : YandexTileProvider {
    private val tileRenderer = YandexTileRendererImpl()
    override fun getTile(x: Int, y: Int, z: Int): ByteArray {
        val center = tileIdMercator.getPointByTileId(TileId(x + 0.5, y + 0.5, z))
        //println("lat: ${center.lat}, lng: ${center.lng}")

        val maxDistance = listOf(
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y.toDouble(), z)),
            tileIdMercator.getPointByTileId(TileId(x.toDouble(), y + 1.0, z)),
            tileIdMercator.getPointByTileId(TileId(x + 1.0, y + 1.0, z))
        ).maxOfOrNull { distance.distance(it, center) }!!

        val searchDistance = maxDistance * 2.0
        val marksResult = markService.getMarks(center.lat, center.lng, searchDistance.toLong())

        val tileSize = tileRenderer.size
        val points = marksResult.points.map {
            val geoPoint = GeoPoint(it.point!!.y, it.point!!.x)
            val tileId = tileIdMercator.getTileIdByPoint(geoPoint, z)
            TileRenderer.XYZDoublePoint(
                (tileSize * (tileId.x - x.toDouble())).toInt(),
                (tileSize * (tileId.y - y.toDouble())).toInt(),
                it.mark!! / 5.0
            )
        }
        val radius = marksResult.distance.toDouble() * tileSize.toDouble() / maxDistance / cos(45.0) / 1.5

        return tileRenderer.renderTile(points, radius.toInt())
    }
}