package com.parvez.calculator.tools

import kotlin.math.*

data class LinearRegressionResult(
    val slope: Double,
    val intercept: Double,
    val correlation: Double,
    val rSquared: Double
)

object ProbabilityEngine {
    fun binomialPmf(n: Int, k: Int, probability: Double): Double {
        require(n >= 0 && k >= 0 && k <= n && probability in 0.0..1.0)
        val r = minOf(k, n - k)
        var combination = 1.0
        for (i in 1..r) combination *= (n - r + i).toDouble() / i
        return combination * probability.pow(k) * (1.0 - probability).pow(n - k)
    }

    fun poissonPmf(k: Int, lambda: Double): Double {
        require(k >= 0 && lambda >= 0.0 && lambda.isFinite())
        if (lambda == 0.0) return if (k == 0) 1.0 else 0.0
        var logFactorial = 0.0
        for (i in 2..k) logFactorial += ln(i.toDouble())
        val logP = k * ln(lambda) - lambda - logFactorial
        return exp(logP)
    }

    fun uniformPdf(x: Double, lower: Double, upper: Double): Double {
        require(lower.isFinite() && upper.isFinite() && lower < upper && x.isFinite())
        return if (x in lower..upper) 1.0 / (upper - lower) else 0.0
    }

    fun uniformCdf(x: Double, lower: Double, upper: Double): Double {
        require(lower.isFinite() && upper.isFinite() && lower < upper && x.isFinite())
        return when {
            x < lower -> 0.0
            x >= upper -> 1.0
            else -> (x - lower) / (upper - lower)
        }
    }

    fun exponentialPdf(x: Double, rate: Double): Double {
        require(rate > 0.0 && rate.isFinite() && x.isFinite())
        return if (x < 0.0) 0.0 else rate * exp(-rate * x)
    }

    fun exponentialCdf(x: Double, rate: Double): Double {
        require(rate > 0.0 && rate.isFinite() && x.isFinite())
        return if (x < 0.0) 0.0 else 1.0 - exp(-rate * x)
    }

    fun normalPdf(x: Double, mean: Double = 0.0, standardDeviation: Double = 1.0): Double {
        require(x.isFinite() && mean.isFinite() && standardDeviation > 0.0 && standardDeviation.isFinite())
        val z = (x - mean) / standardDeviation
        return exp(-0.5 * z * z) / (standardDeviation * sqrt(2.0 * Math.PI))
    }

    /** Approximation to the normal CDF; absolute error is typically below 8e-8. */
    fun normalCdf(x: Double, mean: Double = 0.0, standardDeviation: Double = 1.0): Double {
        require(x.isFinite() && mean.isFinite() && standardDeviation > 0.0 && standardDeviation.isFinite())
        val z = (x - mean) / standardDeviation
        val sign = if (z < 0.0) -1.0 else 1.0
        val t = 1.0 / (1.0 + 0.2316419 * abs(z))
        val polynomial = t * (0.319381530 + t * (-0.356563782 + t * (1.781477937 +
            t * (-1.821255978 + t * 1.330274429))))
        val upperTail = normalPdf(abs(z)) * polynomial
        return (1.0 + sign * (1.0 - 2.0 * upperTail)) / 2.0
    }

    fun linearRegression(x: List<Double>, y: List<Double>): LinearRegressionResult {
        require(x.size == y.size && x.size >= 2) { "Provide at least two paired data points" }
        require(x.all { it.isFinite() } && y.all { it.isFinite() })
        val meanX = x.average()
        val meanY = y.average()
        val sxx = x.sumOf { (it - meanX) * (it - meanX) }
        val syy = y.sumOf { (it - meanY) * (it - meanY) }
        val sxy = x.indices.sumOf { (x[it] - meanX) * (y[it] - meanY) }
        require(sxx > 0.0) { "Regression requires varying x values" }
        val slope = sxy / sxx
        val intercept = meanY - slope * meanX
        val correlation = if (syy == 0.0) 0.0 else sxy / sqrt(sxx * syy)
        return LinearRegressionResult(slope, intercept, correlation, correlation * correlation)
    }
}
