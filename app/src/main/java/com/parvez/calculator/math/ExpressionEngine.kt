package com.parvez.calculator.math

import kotlin.math.*

/**
 * Small safe mathematical parser. This parses a math grammar and never executes code.
 * Supported angle modes: degrees, radians and gradians.
 */
class ExpressionEngine(var angleMode: AngleMode = AngleMode.DEGREES) {
    enum class AngleMode { DEGREES, RADIANS, GRADIANS }

    fun evaluate(source: String, ans: Double = 0.0): Double {
        val parser = Parser(source, angleMode, ans)
        val value = parser.parseExpression()
        parser.skipWhitespace()
        if (!parser.atEnd()) throw IllegalArgumentException("Unexpected input at position ${parser.position + 1}")
        if (!value.isFinite()) throw ArithmeticException("Result is outside the supported numeric range")
        return value
    }

    private class Parser(
        private val input: String,
        private val angleMode: AngleMode,
        private val ans: Double
    ) {
        var position: Int = 0
            private set

        fun atEnd() = position >= input.length
        fun skipWhitespace() { while (!atEnd() && input[position].isWhitespace()) position++ }
        fun parseExpression(): Double = parseAddSub()

        private fun parseAddSub(): Double {
            var value = parseMultiplyDivide()
            while (true) {
                skipWhitespace()
                value = when {
                    consume('+') -> value + parseMultiplyDivide()
                    consume('-') -> value - parseMultiplyDivide()
                    else -> return value
                }
            }
        }

        private fun parseMultiplyDivide(): Double {
            var value = parseUnary()
            while (true) {
                skipWhitespace()
                value = when {
                    consume('*') || consume('×') -> value * parseUnary()
                    consume('/') || consume('÷') -> {
                        val denominator = parseUnary()
                        if (denominator == 0.0) throw ArithmeticException("Cannot divide by zero")
                        value / denominator
                    }
                    consume('%') -> value % parseUnary()
                    else -> if (startsPrimary()) value * parseUnary() else return value
                }
            }
        }

        private fun parseUnary(): Double {
            skipWhitespace()
            return when {
                consume('+') -> parseUnary()
                consume('-') -> -parseUnary()
                else -> parsePower()
            }
        }

        private fun parsePower(): Double {
            val base = parsePostfix()
            skipWhitespace()
            val value = if (consume('^')) base.pow(parseUnary()) else base
            if (!value.isFinite()) throw ArithmeticException("Invalid or out-of-range result")
            return value
        }

        private fun parsePostfix(): Double {
            var value = parsePrimary()
            while (true) {
                skipWhitespace()
                if (!consume('!')) return value
                if (value < 0 || value % 1.0 != 0.0 || value > 170)
                    throw IllegalArgumentException("Factorial requires an integer from 0 to 170")
                var factorial = 1.0
                for (n in 2..value.toInt()) factorial *= n
                value = factorial
            }
        }

        private fun parsePrimary(): Double {
            skipWhitespace()
            if (consume('(')) {
                val value = parseExpression()
                if (!consume(')')) throw IllegalArgumentException("Missing closing parenthesis")
                return value
            }
            if (!atEnd() && (input[position].isDigit() || input[position] == '.')) return parseNumber()

            val identifier = parseIdentifier()
            if (identifier.isEmpty()) throw IllegalArgumentException("Expected a number or function")
            val name = identifier.lowercase()
            if (name == "pi" || name == "π") return Math.PI
            if (name == "e") return Math.E
            if (name == "tau" || name == "τ") return 2.0 * Math.PI
            if (name == "phi" || name == "φ") return (1.0 + sqrt(5.0)) / 2.0
            if (name == "ans") return ans

            if (!consume('(')) throw IllegalArgumentException("Unknown constant or function: $identifier")
            val args = mutableListOf<Double>()
            if (!consume(')')) {
                do {
                    args += parseExpression()
                } while (consume(','))
                if (!consume(')')) throw IllegalArgumentException("Expected ',' or ')'")
            }
            return call(name, args)
        }

        private fun call(name: String, a: List<Double>): Double {
            fun one(): Double {
                if (a.size != 1) throw IllegalArgumentException("$name requires one argument")
                return a[0]
            }
            fun angle(x: Double) = when (angleMode) {
                AngleMode.DEGREES -> Math.toRadians(x)
                AngleMode.RADIANS -> x
                AngleMode.GRADIANS -> x * Math.PI / 200.0
            }
            fun outAngle(x: Double) = when (angleMode) {
                AngleMode.DEGREES -> Math.toDegrees(x)
                AngleMode.RADIANS -> x
                AngleMode.GRADIANS -> x * 200.0 / Math.PI
            }
            val x = a.firstOrNull() ?: 0.0
            val result = when (name) {
                "sin" -> sin(angle(one()))
                "cos" -> cos(angle(one()))
                "tan" -> tan(angle(one()))
                "asin", "arcsin" -> outAngle(asin(one()))
                "acos", "arccos" -> outAngle(acos(one()))
                "atan", "arctan" -> outAngle(atan(one()))
                "sinh" -> sinh(one())
                "cosh" -> cosh(one())
                "tanh" -> tanh(one())
                "asinh" -> ln(one() + sqrt(one() * one() + 1.0))
                "abs" -> abs(one())
                "sqrt" -> sqrt(one())
                "cbrt" -> Math.cbrt(one())
                "ln" -> ln(one())
                "log", "log10" -> log10(one())
                "log2" -> ln(one()) / ln(2.0)
                "exp" -> exp(one())
                "floor" -> floor(one())
                "ceil", "ceiling" -> ceil(one())
                "round" -> round(one())
                "trunc" -> one().toLong().toDouble()
                "sign" -> sign(one())
                "min" -> { if (a.size != 2) throw IllegalArgumentException("min requires two arguments"); min(a[0], a[1]) }
                "max" -> { if (a.size != 2) throw IllegalArgumentException("max requires two arguments"); max(a[0], a[1]) }
                "pow" -> { if (a.size != 2) throw IllegalArgumentException("pow requires two arguments"); a[0].pow(a[1]) }
                "root" -> {
                    if (a.size != 2 || a[1] == 0.0) throw IllegalArgumentException("root requires a value and non-zero degree")
                    val degree = a[1]
                    if (x < 0.0 && degree % 2.0 == 0.0) throw ArithmeticException("Even root of a negative number is not real")
                    if (x < 0.0) -((-x).pow(1.0 / degree)) else x.pow(1.0 / degree)
                }
                "atan2" -> { if (a.size != 2) throw IllegalArgumentException("atan2 requires two arguments"); outAngle(atan2(a[0], a[1])) }
                else -> throw IllegalArgumentException("Unsupported function: $name")
            }
            if (!result.isFinite()) throw ArithmeticException("Function domain error or result overflow")
            return result
        }

        private fun parseNumber(): Double {
            val start = position
            var seenDot = false
            while (!atEnd()) {
                val c = input[position]
                if (c.isDigit()) position++
                else if (c == '.' && !seenDot) { seenDot = true; position++ }
                else break
            }
            if (!atEnd() && (input[position] == 'e' || input[position] == 'E')) {
                position++
                if (!atEnd() && (input[position] == '+' || input[position] == '-')) position++
                val expStart = position
                while (!atEnd() && input[position].isDigit()) position++
                if (expStart == position) throw IllegalArgumentException("Invalid scientific notation")
            }
            return input.substring(start, position).toDoubleOrNull()
                ?: throw IllegalArgumentException("Invalid number")
        }

        private fun parseIdentifier(): String {
            skipWhitespace()
            val start = position
            while (!atEnd() && (input[position].isLetterOrDigit() || input[position] in listOf('π', 'τ', 'φ'))) position++
            return input.substring(start, position)
        }

        private fun startsPrimary(): Boolean {
            skipWhitespace()
            if (atEnd()) return false
            val c = input[position]
            return c.isDigit() || c == '.' || c == '(' || c.isLetter() || c in listOf('π', 'τ', 'φ')
        }

        private fun consume(c: Char): Boolean {
            skipWhitespace()
            if (!atEnd() && input[position] == c) { position++; return true }
            return false
        }
    }
}
