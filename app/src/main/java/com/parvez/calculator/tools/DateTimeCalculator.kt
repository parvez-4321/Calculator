package com.parvez.calculator.tools

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object DateTimeCalculator {
    fun parseDate(isoDate: String): LocalDate =
        runCatching { LocalDate.parse(isoDate.trim()) }
            .getOrElse { throw IllegalArgumentException("Use date format YYYY-MM-DD") }

    fun addDays(isoDate: String, days: Long): String = parseDate(isoDate).plusDays(days).toString()

    fun daysBetween(startIsoDate: String, endIsoDate: String): Long =
        ChronoUnit.DAYS.between(parseDate(startIsoDate), parseDate(endIsoDate))

    fun isLeapYear(year: Int): Boolean = java.time.Year.isLeap(year.toLong())

    fun dayOfWeek(isoDate: String): String = parseDate(isoDate).dayOfWeek.name
    fun dayOfYear(isoDate: String): Int = parseDate(isoDate).dayOfYear

    fun durationBetween(startTime: String, endTime: String): Duration {
        val start = parseTime(startTime)
        val end = parseTime(endTime)
        return Duration.between(start, end)
    }

    fun formatDuration(duration: Duration): String {
        val seconds = duration.seconds
        val sign = if (seconds < 0) "-" else ""
        val absSeconds = kotlin.math.abs(seconds)
        val hours = absSeconds / 3600
        val minutes = (absSeconds % 3600) / 60
        val remainder = absSeconds % 60
        return "$sign${hours}h ${minutes}m ${remainder}s"
    }

    private fun parseTime(value: String): LocalTime =
        runCatching { LocalTime.parse(value.trim()) }
            .getOrElse { throw IllegalArgumentException("Use time format HH:MM or HH:MM:SS") }
}
