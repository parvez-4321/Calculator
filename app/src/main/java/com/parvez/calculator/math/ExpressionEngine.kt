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
                when {
                    consume('!') -> {
                        if (value < 0 || value % 1.0 != 0.0 || value > 170)
                            throw IllegalArgumentException("Factorial requires an integer from 0 to 170")
                        var factorial = 1.0
                        for (n in 2..value.toInt()) factorial *= n
                        value = factorial
                    }
                    consume('%') -> value /= 100.0
                    else -> return value
                }
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
                "cot" -> 1.0 / tan(angle(one()))
                "sec" -> 1.0 / cos(angle(one()))
                "csc" -> 1.0 / sin(angle(one()))
                "asin", "arcsin" -> outAngle(asin(one()))
                "acos", "arccos" -> outAngle(acos(one()))
                "atan", "arctan" -> outAngle(atan(one()))
                "acot" -> outAngle(atan2(1.0, one()))
                "asec" -> outAngle(acos(1.0 / one()))
                "acsc" -> outAngle(asin(1.0 / one()))
                "sinh" -> sinh(one())
                "cosh" -> cosh(one())
                "tanh" -> tanh(one())
                "coth" -> 1.0 / tanh(one())
                "sech" -> 1.0 / cosh(one())
                "csch" -> 1.0 / sinh(one())
                "asinh" -> ln(one() + sqrt(one() * one() + 1.0))
                "acosh" -> ln(one() + sqrt(one() * one() - 1.0))
                "atanh" -> 0.5 * ln((1.0 + one()) / (1.0 - one()))
                "acoth" -> 0.5 * ln((one() + 1.0) / (one() - 1.0))
                "asech" -> ln(1.0 / one() + sqrt(1.0 / (one() * one()) - 1.0))
                "acsch" -> {
                    val reciprocal = 1.0 / one()
                    sign(reciprocal) * ln(abs(reciprocal) + sqrt(reciprocal * reciprocal + 1.0))
                }
                "abs" -> abs(one())
                "sqrt" -> sqrt(one())
                "cbrt" -> Math.cbrt(one())
                "ln" -> ln(one())
                "log10" -> log10(one())
                "log" -> if (a.size == 1) log10(one()) else {
                    if (a.size != 2 || a[0] <= 0.0 || a[1] <= 0.0 || a[1] == 1.0)
                        throw IllegalArgumentException("log requires a positive value and a positive base other than 1")
                    ln(a[0]) / ln(a[1])
                }
                "logbase" -> {
                    if (a.size != 2 || a[0] <= 0.0 || a[1] <= 0.0 || a[1] == 1.0)
                        throw IllegalArgumentException("logbase requires a positive value and a positive base other than 1")
                    ln(a[0]) / ln(a[1])
                }
                "log2" -> ln(one()) / ln(2.0)
                "exp" -> exp(one())
                "floor" -> floor(one())
                "ceil", "ceiling" -> ceil(one())
                "round" -> round(one())
                "trunc" -> one().toLong().toDouble()
                "sign" -> sign(one())
                "min" -> { if (a.size != 2) throw IllegalArgumentException("min requires two arguments"); min(a[0], a[1]) }
                "max" -> { if (a.size != 2) throw IllegalArgumentException("max requires two arguments"); max(a[0], a[1]) }
                "mod" -> { if (a.size != 2 || a[1] == 0.0) throw IllegalArgumentException("mod requires two arguments and a non-zero divisor"); a[0] % a[1] }
                "gcd" -> {
                    if (a.size != 2 || a.any { it % 1.0 != 0.0 || abs(it) > Long.MAX_VALUE.toDouble() })
                        throw IllegalArgumentException("gcd requires two integers")
                    var left = abs(a[0].toLong()); var right = abs(a[1].toLong())
                    while (right != 0L) { val remainder = left % right; left = right; right = remainder }
                    left.toDouble()
                }
                "lcm" -> {
                    if (a.size != 2 || a.any { it % 1.0 != 0.0 || abs(it) > Long.MAX_VALUE.toDouble() })
                        throw IllegalArgumentException("lcm requires two integers")
                    val left = abs(a[0].toLong()); val right = abs(a[1].toLong())
                    if (left == 0L || right == 0L) 0.0 else {
                        var x1 = left; var y1 = right
                        while (y1 != 0L) { val remainder = x1 % y1; x1 = y1; y1 = remainder }
                        (left / x1 * right).toDouble()
                    }
                }
                "perm", "npr", "comb", "ncr" -> {
                    if (a.size != 2 || a.any { it < 0.0 || it % 1.0 != 0.0 || it > 170.0 })
                        throw IllegalArgumentException("$name requires non-negative integers no greater than 170")
                    val n = a[0].toInt(); val r = a[1].toInt()
                    if (r > n) 0.0 else {
                        val k = if (name == "comb" || name == "ncr") minOf(r, n - r) else r
                        var resultValue = 1.0
                        for (i in 0 until k) resultValue *= (n - i).toDouble() / (if (name == "comb" || name == "ncr") (i + 1).toDouble() else 1.0)
                        resultValue
                    }
                }
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
                var lookahead = position + 1
                if (lookahead < input.length && (input[lookahead] == '+' || input[lookahead] == '-')) lookahead++
                if (lookahead < input.length && input[lookahead].isDigit()) {
                    position = lookahead
                    while (!atEnd() && input[position].isDigit()) position++
                }
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
