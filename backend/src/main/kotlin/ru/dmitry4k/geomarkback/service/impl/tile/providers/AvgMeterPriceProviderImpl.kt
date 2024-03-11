package ru.dmitry4k.geomarkback.service.impl.tile.providers

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.geo.Distance
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator
import ru.dmitry4k.geomarkback.service.tile.TileRenderer

@Service
class AvgMeterPriceProviderImpl(
    tileIdMercator: TileIdMercator,
    distance: Distance,
    markService: MarksService,
    tileRenderer: TileRenderer,
): AbstractAvgValueTileProvdier(tileIdMercator, distance, markService, tileRenderer) {
    override fun getValue(point: GeoPointDao): Double = with(point.rates.avgMeterPrice) {
        if (count == 0L) 0.0 else value
    }

    override fun getMaxValue(): Double = 3462086.9565217393

    override fun getMinValue(): Double = 58947.36842105263

    override fun layerName(): String = "avgMeterPrice"
}
