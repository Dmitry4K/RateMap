package ru.dmitry4k.geomarkback.math

import java.lang.Exception
import kotlin.math.acos
import kotlin.math.sin

class MathEngine {
    class MathException(message: String) : Exception(message)
    companion object {
        @Throws(MathException::class)
        fun getCircleAreaIntersection(r1: Double, r2: Double, dist: Double): Double {
            if (r1 + r2 >= dist) {
                throw MathException("Two circles do not intersect")
            }
            val f1 = 2 * acos((r1 * r1 - r2 * r2 + dist * dist) / (2 * r1 * dist))
            val f2 = 2 * acos((r2 * r2 - r1 * r1 + dist * dist) / (2 * r2 * dist))
            val s1 = r1 * r1 * (f1 - sin(f1)) / 2
            val s2 = r2 * r2 * (f2 - sin(f2)) / 2
            return s1 + s2
        }
    }
}