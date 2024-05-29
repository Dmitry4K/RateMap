package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.tile.TileModifier
import ru.dmitry4k.geomarkback.service.tile.TileSettingsProvider
import java.awt.Color

@Primary
@Service
class MasterModifier(tileSettingsProvider: TileSettingsProvider): TileModifier<Double, Color> {

    val borderModifier = BorderModifier(tileSettingsProvider.getBorderColor())
    val colorModifier = ColorModifier(tileSettingsProvider.getLowestColor(), tileSettingsProvider.getHighestColor())
    val squareDoubleTileValidator = SquareDoubleTileValidator(tileSettingsProvider.getTileSize())
    val stepInterpolationModifier = StepInterpolationModifier(tileSettingsProvider.getStepValue())

    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Color>> {
        return matrix
            .let { squareDoubleTileValidator.modify(it) }
//            .let { stepInterpolationModifier.modify(it) }
            .let { colorModifier.modify(it) }
//            .let { borderModifier.modify(it) }
    }
}