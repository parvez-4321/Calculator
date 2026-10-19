package com.parvez.calculator.tools

enum class NumberBase(val radix: Int) { BINARY(2), OCTAL(8), DECIMAL(10), HEXADECIMAL(16) }

object ProgrammerCalculator {
    fun convert(input: String, from: NumberBase, to: NumberBase): String {
        val cleaned = input.trim().replace("_", "")
        require(cleaned.isNotEmpty()) { "Enter a number" }
        val value = cleaned.toLongOrNull(from.radix) ?: throw IllegalArgumentException("Invalid base-${from.radix} integer")
        return value.toString(to.radix).uppercase()
    }

    fun and(a: Long, b: Long) = a and b
    fun or(a: Long, b: Long) = a or b
    fun xor(a: Long, b: Long) = a xor b
    fun not(a: Long) = a.inv()
    fun shiftLeft(value: Long, distance: Int): Long {
        require(distance in 0..63)
        return value shl distance
    }
    fun shiftRight(value: Long, distance: Int): Long {
        require(distance in 0..63)
        return value shr distance
    }
    fun unsignedShiftRight(value: Long, distance: Int): Long {
        require(distance in 0..63)
        return value ushr distance
    }
}
