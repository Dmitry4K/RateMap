package ru.dmitry4k.geomarkback.service.impl.tile.providers

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.data.dao.GeoPointDao
import ru.dmitry4k.geomarkback.service.MarksService
import ru.dmitry4k.geomarkback.service.geo.Distance
import ru.dmitry4k.geomarkback.service.geo.TileIdMercator
import ru.dmitry4k.geomarkback.service.tile.LegendRenderer
import ru.dmitry4k.geomarkback.service.tile.TileRenderer
import ru.dmitry4k.geomarkback.service.tile.TileSettingsProvider
import kotlin.math.max
import kotlin.math.min

@Service
class ComposeMapLayer(
    tileIdMercator: TileIdMercator,
    distance: Distance,
    markService: MarksService,
    tileRenderer: TileRenderer,
    legendRenderer: LegendRenderer,
    tileSettingsProvider: TileSettingsProvider,
    val avgMeterPriceProvder: AvgMeterPriceProviderImpl,
    val marksAvgProvider: MarksAvgValueTileProviderImpl
): AbstractAvgValueTileProvdier(
    tileIdMercator,
    distance,
    markService,
    tileRenderer,
    legendRenderer,
    tileSettingsProvider
) {
    override fun getValue(point: GeoPointDao): Double {
        val mark = marksAvgProvider.getValue(point)
        val avgMeter = normAvgMeterPrice(
            avgMeterPriceProvder.getValue(point),
            avgMeterPriceProvder.getMinValue(),
            avgMeterPriceProvder.getMaxValue()
        )
        return 0.5 * mark + 0.5 * avgMeter
    }
    override fun getMaxValue(): Double = 5.0

    override fun getMinValue(): Double = 0.0

    override fun getWeight(point: GeoPointDao): Double {
        val avgMeter = norm(avgMeterPriceProvder.getWeight(point), avgMeterPriceProvder.getMinWeight(), avgMeterPriceProvder.getMaxWeight())
        val mark = norm(marksAvgProvider.getWeight(point), marksAvgProvider.getMinWeight(), marksAvgProvider.getMaxWeight())
        return 0.5 * mark + 0.5 * avgMeter
    }

    override fun getMaxWeight(): Double = 1.0

    override fun getMinWeight(): Double = 0.0

    override fun layerName() = "composite"

    private fun normAvgMeterPrice(value: Double, a: Double, b: Double): Double {
        return 5.0 * (1.0 - norm(value, a , b))
    }

    private fun norm(value: Double, a: Double, b: Double): Double {
        val min = min(a, b)
        val max = max(a, b)
        return (value - min) / (max - min)
    }
}