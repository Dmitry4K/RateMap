package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.service.tile.TileModifier
import java.awt.Color

@Primary
@Service
class MasterModifier: TileModifier<Double, Color> {
    val borderModifier = BorderModifier()
    val colorModifier = ColorModifier()
    val squareDoubleTileValidator = SquareDoubleTileValidator()
    val stepInterpolationModifier = StepInterpolationModifier()
    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Color>> {
        return matrix
            .let { squareDoubleTileValidator.modify(it) }
            .let { stepInterpolationModifier.modify(it) }
            .let { colorModifier.modify(it) }
            //.let { borderModifier.modify(it) }
    }
}