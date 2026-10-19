package com.parvez.calculator.tools

import kotlin.math.abs

object MatrixEngine {
    private fun validate(a: Array<DoubleArray>) {
        require(a.isNotEmpty() && a[0].isNotEmpty()) { "Matrix cannot be empty" }
        val cols = a[0].size
        require(a.all { it.size == cols }) { "Matrix rows must have equal lengths" }
        require(a.all { row -> row.all { it.isFinite() } }) { "Matrix entries must be finite" }
    }

    fun add(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        validate(a); validate(b)
        require(a.size == b.size && a[0].size == b[0].size) { "Matrix dimensions must match" }
        return Array(a.size) { i -> DoubleArray(a[0].size) { j -> a[i][j] + b[i][j] } }
    }

    fun multiply(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        validate(a); validate(b)
        require(a[0].size == b.size) { "Columns of the first matrix must equal rows of the second" }
        return Array(a.size) { i ->
            DoubleArray(b[0].size) { j -> (0 until b.size).sumOf { k -> a[i][k] * b[k][j] } }
        }
    }

    fun transpose(a: Array<DoubleArray>): Array<DoubleArray> {
        validate(a)
        return Array(a[0].size) { j -> DoubleArray(a.size) { i -> a[i][j] } }
    }

    fun determinant(matrix: Array<DoubleArray>): Double {
        validate(matrix)
        require(matrix.size == matrix[0].size) { "Determinant requires a square matrix" }
        val a = Array(matrix.size) { matrix[it].clone() }
        var sign = 1.0
        var det = 1.0
        for (col in a.indices) {
            var pivot = col
            for (row in col + 1 until a.size) if (abs(a[row][col]) > abs(a[pivot][col])) pivot = row
            if (abs(a[pivot][col]) < 1e-12) return 0.0
            if (pivot != col) { val tmp = a[pivot]; a[pivot] = a[col]; a[col] = tmp; sign *= -1 }
            val p = a[col][col]
            det *= p
            for (row in col + 1 until a.size) {
                val factor = a[row][col] / p
                for (j in col + 1 until a.size) a[row][j] -= factor * a[col][j]
            }
        }
        return sign * det
    }

    fun inverse(matrix: Array<DoubleArray>): Array<DoubleArray> {
        validate(matrix)
        require(matrix.size == matrix[0].size) { "Inverse requires a square matrix" }
        val n = matrix.size
        val a = Array(n) { i -> DoubleArray(2 * n) { j ->
            when {
                j < n -> matrix[i][j]
                j - n == i -> 1.0
                else -> 0.0
            }
        } }
        for (col in 0 until n) {
            var pivot = col
            for (row in col + 1 until n) if (abs(a[row][col]) > abs(a[pivot][col])) pivot = row
            require(abs(a[pivot][col]) >= 1e-12) { "Matrix is singular and has no inverse" }
            val tmp = a[pivot]; a[pivot] = a[col]; a[col] = tmp
            val divisor = a[col][col]
            for (j in 0 until 2 * n) a[col][j] /= divisor
            for (row in 0 until n) if (row != col) {
                val factor = a[row][col]
                for (j in 0 until 2 * n) a[row][j] -= factor * a[col][j]
            }
        }
        return Array(n) { i -> a[i].copyOfRange(n, 2 * n) }
    }

    fun trace(a: Array<DoubleArray>): Double {
        validate(a)
        require(a.size == a[0].size) { "Trace requires a square matrix" }
        return a.indices.sumOf { a[it][it] }
    }
}
