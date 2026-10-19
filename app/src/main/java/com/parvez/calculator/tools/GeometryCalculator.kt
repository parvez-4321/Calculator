package com.parvez.calculator.tools

import kotlin.math.PI
import kotlin.math.sqrt

object GeometryCalculator {
    fun circleArea(radius: Double): Double { requireNonNegative(radius); return PI * radius * radius }
    fun circleCircumference(radius: Double): Double { requireNonNegative(radius); return 2.0 * PI * radius }
    fun rectangleArea(width: Double, height: Double): Double {
        requireNonNegative(width); requireNonNegative(height); return width * height
    }
    fun triangleArea(base: Double, height: Double): Double {
        requireNonNegative(base); requireNonNegative(height); return base * height / 2.0
    }
    fun sphereVolume(radius: Double): Double {
        requireNonNegative(radius); return 4.0 / 3.0 * PI * radius * radius * radius
    }
    fun cylinderVolume(radius: Double, height: Double): Double {
        requireNonNegative(radius); requireNonNegative(height); return PI * radius * radius * height
    }
    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        require(listOf(x1, y1, x2, y2).all { it.isFinite() })
        return sqrt((x2 - x1).pow2() + (y2 - y1).pow2())
    }
    fun pythagorean(a: Double, b: Double): Double {
        requireNonNegative(a); requireNonNegative(b); return sqrt(a * a + b * b)
    }
    private fun requireNonNegative(value: Double) { require(value.isFinite() && value >= 0.0) }
    private fun Double.pow2() = this * this
}
