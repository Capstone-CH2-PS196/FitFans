package com.capstonech2.fitfans.utils

import kotlin.math.pow
import kotlin.math.round

fun calculateBMI(weight: Double, height: Double): Double = round((weight / (height / 100).pow(2)) * 10) / 10.0

fun convertMillisToMinutesSeconds(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d m %02d s", minutes, seconds)
}

fun convertToMinutes(milliseconds: Long): Long {
    val totalSeconds = milliseconds / 1000
    return totalSeconds / 60
}

fun calculateCalories(minutes: Long, equipment: String, level: String, weight: Double): Double {
    val met: Double = when (equipment) {
        "Barbell" -> {
            when (level) {
                "Beginner" -> 3.0
                "Ideal" -> 4.0
                "Expert" -> 6.0
                else -> 0.0
            }
        }
        "Dumbbell" -> {
            when (level) {
                "Beginner" -> 3.0
                "Ideal" -> 4.0
                "Expert" -> 6.0
                else -> 0.0
            }
        }
        "Gym Ball" -> {
            when (level) {
                "Beginner" -> 2.5
                "Ideal" -> 3.0
                "Expert" -> 5.0
                else -> 0.0
            }
        }
        "Kettlebell" -> {
            when (level) {
                "Beginner" -> 4.0
                "Ideal" -> 5.0
                "Expert" -> 7.0
                else -> 0.0
            }
        }
        "Roller Abs" -> {
            when (level) {
                "Beginner" -> 4.0
                "Ideal" -> 5.0
                "Expert" -> 7.0
                else -> 0.0
            }
        }
        "Leg Press" -> {
            when (level) {
                "Beginner" -> 3.5
                "Ideal" -> 4.0
                "Expert" -> 6.0
                else -> 0.0
            }
        }
        "Punching Bag" -> {
            when (level) {
                "Beginner" -> 5.5
                "Ideal" -> 6.0
                "Expert" -> 8.0
                else -> 0.0
            }
        }
        "Static Bicycle" -> {
            when (level) {
                "Beginner" -> 3.5
                "Ideal" -> 4.0
                "Expert" -> 7.0
                else -> 0.0
            }
        }
        "Step" -> {
            when (level) {
                "Beginner" -> 4.5
                "Ideal" -> 5.0
                "Expert" -> 8.0
                else -> 0.0
            }
        }
        "Treadmill" -> {
            when (level) {
                "Beginner" -> 3.0
                "Ideal" -> 5.0
                "Expert" -> 9.0
                else -> 0.0
            }
        }
        else -> 0.0
    }
    return round((minutes * (met * 3.5 * weight) / 200.0) * 10.0) / 10.0
}
