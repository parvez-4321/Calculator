package com.parvez.calculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class CalculationHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val result: String,
    val rawResult: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val angleMode: String = "DEGREES",
    val pinned: Boolean = false,
    val note: String = ""
)
