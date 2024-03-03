package ru.dmitry4k.geomarkback.service.impl.tile.modifiers

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import ru.dmitry4k.geomarkback.extension.withAlpha
import ru.dmitry4k.geomarkback.service.tile.TileModifier
import java.awt.Color

@Primary
@Service
class MasterModifier: TileModifier<Double, Color> {
    private val alphaFloat: Double = 0.7
    private val alphaInt: Int = (alphaFloat * 256).toInt()
    private val minColor: Color = Color.RED.withAlpha(alphaInt)
    private val maxColor: Color = Color.GREEN.withAlpha(alphaInt)
    private val borderColor: Color = Color.BLACK.withAlpha(alphaInt)
    private val stepValue = 0.05

    val borderModifier = BorderModifier(borderColor)
    val colorModifier = ColorModifier(minColor, maxColor, alphaInt)
    val squareDoubleTileValidator = SquareDoubleTileValidator()
    val stepInterpolationModifier = StepInterpolationModifier(stepValue)
    override fun modify(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Color>> {
        return matrix
            .let { squareDoubleTileValidator.modify(it) }
            .let { stepInterpolationModifier.modify(it) }
            .let { colorModifier.modify(it) }
            .let { borderModifier.modify(it) }
    }
}