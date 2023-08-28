package ru.dmitry4k.geomarkback.math

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.abs

class MathEngineTest {
    @Test
    fun `Test getting intersection of 2 circles`() {
        assertTrue(
            abs(9.57019947298 - MathEngine.getCircleAreaIntersection(4.0, 2.0, 3.0)) < 0.001
        )
    }

    @Test
    fun `Test getting exception when two circles do not intersect`() {
        assertThrows<MathEngine.MathException> { MathEngine.getCircleAreaIntersection(4.0, 2.0, 6.0) }
    }
}