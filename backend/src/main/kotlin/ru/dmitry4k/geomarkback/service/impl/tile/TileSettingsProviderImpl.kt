package ru.dmitry4k.geomarkback.service.impl.tile

import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.extension.withAlpha
import ru.dmitry4k.geomarkback.service.tile.TileSettingsProvider
import java.awt.Color

@Service
class TileSettingsProviderImpl: TileSettingsProvider {
    private val alphaFloat: Double = 0.8
    private val alphaInt: Int = (alphaFloat * 256).toInt()

    override fun getTileSize(): Int = 64

    override fun getLowestColor(): Color = Color.RED.withAlpha(alphaInt)

    override fun getHighestColor(): Color = Color.GREEN.withAlpha(alphaInt)

    override fun getBorderColor(): Color = Color.BLACK.withAlpha(80)

    override fun getBorderWidth(): Int = 1
    override fun getStepValue(): Double = 0.15
}
