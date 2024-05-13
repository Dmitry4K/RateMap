package ru.dmitry4k.geomarkback.service.impl.tile.providers

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.geo.Distance
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator
import ru.dmitry4k.geomarkback.service.tile.LegendRenderer
import ru.dmitry4k.geomarkback.service.tile.TileRenderer
import ru.dmitry4k.geomarkback.service.tile.TileSettingsProvider

@Service
class AvgMeterPriceProviderImpl(
    tileIdMercator: TileIdMercator,
    distance: Distance,
    markService: MarksService,
    tileRenderer: TileRenderer,
    legendRenderer: LegendRenderer,
    tileSettingsProvider: TileSettingsProvider
): AbstractAvgValueTileProvdier(
    tileIdMercator,
    distance,
    markService,
    tileRenderer,
    legendRenderer,
    tileSettingsProvider
) {
    override fun getValue(point: GeoPointDao): Double = with(point.rates.avgMeterPrice) {
        if (count == 0L) (getMaxValue() + getMinValue()) / 2.0 else value
    }

    override fun getMaxValue(): Double = 1000000.0 // 3462086.9565217393

    override fun getMinValue(): Double = 100000.0 // 58947.36842105263

    override fun layerName(): String = "avgMeterPrice"
}
