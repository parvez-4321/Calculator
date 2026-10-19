package com.parvez.calculator.tools

import kotlin.math.sqrt

data class DescriptiveStatistics(
    val count: Int,
    val sum: Double,
    val mean: Double,
    val median: Double,
    val minimum: Double,
    val maximum: Double,
    val range: Double,
    val sampleVariance: Double,
    val populationVariance: Double,
    val sampleStandardDeviation: Double,
    val populationStandardDeviation: Double,
    val q1: Double,
    val q3: Double,
    val interquartileRange: Double
)

object StatisticsEngine {
    fun describe(values: List<Double>): DescriptiveStatistics {
        require(values.isNotEmpty()) { "Enter at least one value" }
        require(values.all { it.isFinite() }) { "All values must be finite numbers" }
        val sorted = values.sorted()
        val n = sorted.size
        val sum = values.sum()
        val mean = sum / n
        val median = medianOf(sorted)
        val lower = if (n == 1) sorted else sorted.subList(0, n / 2)
        val upper = if (n == 1) sorted else sorted.subList((n + 1) / 2, n)
        val q1 = medianOf(lower)
        val q3 = medianOf(upper)
        val populationVariance = values.sumOf { (it - mean) * (it - mean) } / n
        val sampleVariance = if (n > 1) values.sumOf { (it - mean) * (it - mean) } / (n - 1) else 0.0
        return DescriptiveStatistics(
            n, sum, mean, median, sorted.first(), sorted.last(), sorted.last() - sorted.first(),
            sampleVariance, populationVariance, sqrt(sampleVariance), sqrt(populationVariance),
            q1, q3, q3 - q1
        )
    }

    fun percentile(values: List<Double>, percentile: Double): Double {
        require(values.isNotEmpty() && values.all { it.isFinite() }) { "Provide finite data values" }
        require(percentile in 0.0..100.0) { "Percentile must be between 0 and 100" }
        val sorted = values.sorted()
        val index = percentile / 100.0 * (sorted.size - 1)
        val lower = index.toInt()
        val upper = kotlin.math.ceil(index).toInt()
        return sorted[lower] + (sorted[upper] - sorted[lower]) * (index - lower)
    }

    fun zScores(values: List<Double>): List<Double> {
        val stats = describe(values)
        require(stats.populationStandardDeviation > 0.0) { "Z-scores are undefined when all values are equal" }
        return values.map { (it - stats.mean) / stats.populationStandardDeviation }
    }

    private fun medianOf(sorted: List<Double>): Double {
        require(sorted.isNotEmpty())
        val mid = sorted.size / 2
        return if (sorted.size % 2 == 0) (sorted[mid - 1] + sorted[mid]) / 2.0 else sorted[mid]
    }
}
