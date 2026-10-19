package com.parvez.calculator.tools

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorToolsTest {
    @Test fun statisticsMeanAndMedian() {
        val stats = StatisticsEngine.describe(listOf(1.0, 2.0, 3.0, 4.0, 5.0))
        assertEquals(3.0, stats.mean, 1e-10)
        assertEquals(3.0, stats.median, 1e-10)
    }

    @Test fun matrixDeterminant() {
        val matrix = arrayOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertEquals(-2.0, MatrixEngine.determinant(matrix), 1e-10)
    }

    @Test fun quadraticRoots() {
        val roots = EquationSolver.solveQuadratic(1.0, -3.0, 2.0) as QuadraticSolutions.Real
        assertEquals(1.0, roots.first, 1e-10)
        assertEquals(2.0, roots.second!!, 1e-10)
    }

    @Test fun convertsTemperature() {
        assertEquals(32.0, UnitConverter.temperature(0.0, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT), 1e-10)
    }

    @Test fun complexMultiplication() {
        val result = ComplexNumber(1.0, 2.0) * ComplexNumber(3.0, 4.0)
        assertEquals(-5.0, result.real, 1e-10)
        assertEquals(10.0, result.imaginary, 1e-10)
    }

    @Test fun programmerBaseConversion() {
        assertEquals("FF", ProgrammerCalculator.convert("255", NumberBase.DECIMAL, NumberBase.HEXADECIMAL))
        assertTrue(ProgrammerCalculator.and(6, 3) == 2L)
    }

    @Test fun probabilityAndRegression() {
        assertEquals(0.5, ProbabilityEngine.binomialPmf(2, 1, 0.5), 1e-10)
        assertEquals(0.5, ProbabilityEngine.uniformCdf(5.0, 0.0, 10.0), 1e-10)
        val regression = ProbabilityEngine.linearRegression(listOf(1.0, 2.0, 3.0), listOf(3.0, 5.0, 7.0))
        assertEquals(2.0, regression.slope, 1e-10)
        assertEquals(1.0, regression.intercept, 1e-10)
        assertEquals(1.0, regression.rSquared, 1e-10)
    }
}
