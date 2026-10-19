package com.parvez.calculator.tools

import kotlin.math.pow

object FinancialCalculator {
    fun simpleInterest(principal: Double, annualRatePercent: Double, years: Double): Double {
        validate(principal, annualRatePercent, years)
        return principal * (annualRatePercent / 100.0) * years
    }

    fun compoundFutureValue(principal: Double, annualRatePercent: Double, compoundsPerYear: Int, years: Double): Double {
        validate(principal, annualRatePercent, years)
        require(compoundsPerYear > 0) { "Compounds per year must be positive" }
        require(annualRatePercent > -100.0) { "Annual rate must be greater than -100%" }
        return principal * (1.0 + annualRatePercent / 100.0 / compoundsPerYear).pow(compoundsPerYear * years)
    }

    fun loanPayment(principal: Double, annualRatePercent: Double, paymentsPerYear: Int, numberOfPayments: Int): Double {
        require(principal >= 0 && principal.isFinite())
        require(annualRatePercent.isFinite() && annualRatePercent >= 0)
        require(paymentsPerYear > 0 && numberOfPayments > 0)
        val rate = annualRatePercent / 100.0 / paymentsPerYear
        return if (rate == 0.0) principal / numberOfPayments
        else principal * rate / (1.0 - (1.0 + rate).pow(-numberOfPayments))
    }

    fun percentageChange(oldValue: Double, newValue: Double): Double {
        require(oldValue.isFinite() && newValue.isFinite() && oldValue != 0.0)
        return (newValue - oldValue) / kotlin.math.abs(oldValue) * 100.0
    }

    fun profitMargin(sellingPrice: Double, cost: Double): Double {
        require(sellingPrice.isFinite() && cost.isFinite() && sellingPrice != 0.0)
        return (sellingPrice - cost) / sellingPrice * 100.0
    }

    private fun validate(principal: Double, rate: Double, years: Double) {
        require(principal >= 0.0 && principal.isFinite())
        require(rate.isFinite() && years >= 0.0 && years.isFinite())
    }
}
