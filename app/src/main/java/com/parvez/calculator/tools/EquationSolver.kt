package com.parvez.calculator.tools

import kotlin.math.abs
import kotlin.math.sqrt

sealed interface QuadraticSolutions {
    data class Real(val first: Double, val second: Double?) : QuadraticSolutions
    data class Complex(val realPart: Double, val imaginaryMagnitude: Double) : QuadraticSolutions
    data object NoSolution : QuadraticSolutions
}

object EquationSolver {
    fun solveLinear(a: Double, b: Double): Double? {
        require(a.isFinite() && b.isFinite())
        return if (a == 0.0) null else -b / a
    }

    fun solveQuadratic(a: Double, b: Double, c: Double): QuadraticSolutions {
        require(listOf(a, b, c).all { it.isFinite() })
        if (a == 0.0) {
            val root = solveLinear(b, c) ?: return QuadraticSolutions.NoSolution
            return QuadraticSolutions.Real(root, null)
        }
        val discriminant = b * b - 4.0 * a * c
        return when {
            discriminant > 0.0 -> {
                val q = -0.5 * (b + if (b >= 0) sqrt(discriminant) else -sqrt(discriminant))
                val r1 = q / a
                val r2 = if (q == 0.0) (-b + sqrt(discriminant)) / (2 * a) else c / q
                QuadraticSolutions.Real(minOf(r1, r2), maxOf(r1, r2))
            }
            discriminant == 0.0 -> QuadraticSolutions.Real(-b / (2.0 * a), null)
            else -> QuadraticSolutions.Complex(-b / (2.0 * a), sqrt(-discriminant) / abs(2.0 * a))
        }
    }

    fun bisection(
        function: (Double) -> Double,
        lowerBound: Double,
        upperBound: Double,
        tolerance: Double = 1e-10,
        maxIterations: Int = 200
    ): Double {
        require(lowerBound.isFinite() && upperBound.isFinite() && lowerBound < upperBound)
        require(tolerance > 0 && tolerance.isFinite() && maxIterations > 0)
        var lo = lowerBound
        var hi = upperBound
        var flo = function(lo)
        var fhi = function(hi)
        require(flo.isFinite() && fhi.isFinite() && flo * fhi <= 0) { "Bounds must bracket a root" }
        repeat(maxIterations) {
            val mid = lo + (hi - lo) / 2.0
            val fm = function(mid)
            if (!fm.isFinite()) throw ArithmeticException("Function returned a non-finite value")
            if (abs(fm) <= tolerance || abs(hi - lo) <= tolerance) return mid
            if (flo * fm <= 0) { hi = mid; fhi = fm } else { lo = mid; flo = fm }
        }
        throw ArithmeticException("Bisection solver did not converge")
    }
}
