package ru.dmitry4k.geomarkback.service.impl.tile.providers

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.geo.Distance
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator
import ru.dmitry4k.geomarkback.service.tile.TileRenderer

@Service
class MarksAvgValueTileProviderImpl(
    tileIdMercator: TileIdMercator,
    distance: Distance,
    markService: MarksService,
    tileRenderer: TileRenderer
) : AbstractAvgValueTileProvdier(tileIdMercator, distance, markService, tileRenderer) {
    override fun getValue(point: GeoPointDao): Double = with(point.rates.mark) {
        if (count == 0L) 2.5 else value
    }

    override fun getMaxValue(): Double = 0.0

    override fun getMinValue(): Double = 5.0

    override fun layerName() = "marks"
}