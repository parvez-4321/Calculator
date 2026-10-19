package com.parvez.calculator.math

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionEngineTest {
    @Test fun precedence() = assertEquals(14.0, ExpressionEngine().evaluate("2+3*4"), 1e-10)
    @Test fun parenthesesAndImplicitMultiplication() = assertEquals(20.0, ExpressionEngine().evaluate("2(3+7)"), 1e-10)
    @Test fun degreeTrig() = assertEquals(0.5, ExpressionEngine().evaluate("sin(30)"), 1e-10)
    @Test fun constantsAndFunctions() = assertEquals(5.0, ExpressionEngine().evaluate("sqrt(9)+2"), 1e-10)
    @Test(expected = ArithmeticException::class) fun divisionByZero() { ExpressionEngine().evaluate("1/0") }
    @Test fun percentageAndCombinatorics() {
        assertEquals(30.0, ExpressionEngine().evaluate("200*15%"), 1e-10)
        assertEquals(230.0, ExpressionEngine().evaluate("200+15%"), 1e-10)
        assertEquals(170.0, ExpressionEngine().evaluate("200-15%"), 1e-10)
        assertEquals(10.0, ExpressionEngine().evaluate("ncr(5,2)"), 1e-10)
        assertEquals(60.0, ExpressionEngine().evaluate("npr(5,3)"), 1e-10)
    }
    @Test fun inverseHyperbolicHandlesNegativeValues() {
        assertEquals(-1.4436354751788103, ExpressionEngine().evaluate("asinh(-2)"), 1e-10)
        assertEquals(-3.0, ExpressionEngine().evaluate("root(-27,3)"), 1e-10)
    }
    @Test(expected = ArithmeticException::class) fun rejectsNonRealFractionalRootOfNegativeValue() {
        ExpressionEngine().evaluate("root(-8,2.5)")
    }
}
