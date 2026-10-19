package com.parvez.calculator.tools

import kotlin.math.*

data class ComplexNumber(val real: Double, val imaginary: Double) {
    init { require(real.isFinite() && imaginary.isFinite()) { "Complex components must be finite" } }

    operator fun plus(other: ComplexNumber) = ComplexNumber(real + other.real, imaginary + other.imaginary)
    operator fun minus(other: ComplexNumber) = ComplexNumber(real - other.real, imaginary - other.imaginary)
    operator fun times(other: ComplexNumber) = ComplexNumber(
        real * other.real - imaginary * other.imaginary,
        real * other.imaginary + imaginary * other.real
    )
    operator fun div(other: ComplexNumber): ComplexNumber {
        val denominator = other.real * other.real + other.imaginary * other.imaginary
        require(denominator != 0.0) { "Cannot divide by zero complex number" }
        return ComplexNumber(
            (real * other.real + imaginary * other.imaginary) / denominator,
            (imaginary * other.real - real * other.imaginary) / denominator
        )
    }
    fun conjugate() = ComplexNumber(real, -imaginary)
    fun magnitude() = hypot(real, imaginary)
    fun argumentRadians() = atan2(imaginary, real)
    fun exp(): ComplexNumber {
        val scale = kotlin.math.exp(real)
        return ComplexNumber(scale * cos(imaginary), scale * sin(imaginary))
    }
    fun principalLog(): ComplexNumber = ComplexNumber(ln(magnitude()), argumentRadians())
}
