package com.parvez.calculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CalculationHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun historyDao(): CalculationHistoryDao

    companion object {
        @Volatile private var instance: CalculatorDatabase? = null

        fun getInstance(context: Context): CalculatorDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CalculatorDatabase::class.java,
                    "calculator.db"
                ).build().also { instance = it }
            }
    }
}
