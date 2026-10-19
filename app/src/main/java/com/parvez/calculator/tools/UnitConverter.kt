package com.parvez.calculator.tools

enum class LengthUnit(val meters: Double) {
    MILLIMETER(0.001), CENTIMETER(0.01), METER(1.0), KILOMETER(1000.0),
    INCH(0.0254), FOOT(0.3048), YARD(0.9144), MILE(1609.344)
}

enum class MassUnit(val kilograms: Double) {
    MILLIGRAM(1e-6), GRAM(0.001), KILOGRAM(1.0), TONNE(1000.0),
    OUNCE(0.028349523125), POUND(0.45359237)
}

enum class TemperatureUnit { CELSIUS, FAHRENHEIT, KELVIN }

object UnitConverter {
    fun length(value: Double, from: LengthUnit, to: LengthUnit): Double {
        require(value.isFinite())
        return value * from.meters / to.meters
    }

    fun mass(value: Double, from: MassUnit, to: MassUnit): Double {
        require(value.isFinite())
        return value * from.kilograms / to.kilograms
    }

    fun temperature(value: Double, from: TemperatureUnit, to: TemperatureUnit): Double {
        require(value.isFinite())
        val celsius = when (from) {
            TemperatureUnit.CELSIUS -> value
            TemperatureUnit.FAHRENHEIT -> (value - 32.0) * 5.0 / 9.0
            TemperatureUnit.KELVIN -> value - 273.15
        }
        return when (to) {
            TemperatureUnit.CELSIUS -> celsius
            TemperatureUnit.FAHRENHEIT -> celsius * 9.0 / 5.0 + 32.0
            TemperatureUnit.KELVIN -> celsius + 273.15
        }
    }

    fun speed(value: Double, fromMetersPerSecond: Double, toMetersPerSecond: Double): Double {
        require(value.isFinite() && fromMetersPerSecond > 0 && toMetersPerSecond > 0)
        return value * fromMetersPerSecond / toMetersPerSecond
    }
}
