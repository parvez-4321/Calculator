package com.parvez.calculator.math

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionEngineTest {
    @Test fun precedence() = assertEquals(14.0, ExpressionEngine().evaluate("2+3*4"), 1e-10)
    @Test fun parenthesesAndImplicitMultiplication() = assertEquals(20.0, ExpressionEngine().evaluate("2(3+7)"), 1e-10)
    @Test fun degreeTrig() = assertEquals(0.5, ExpressionEngine().evaluate("sin(30)"), 1e-10)
    @Test fun constantsAndFunctions() = assertEquals(5.0, ExpressionEngine().evaluate("sqrt(9)+2"), 1e-10)
    @Test(expected = ArithmeticException::class) fun divisionByZero() { ExpressionEngine().evaluate("1/0") }
}
