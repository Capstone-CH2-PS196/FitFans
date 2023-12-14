package com.capstonech2.fitfans.utils

import kotlin.math.pow
import kotlin.math.round

fun calculateBMI(weight: Double, height: Double): Double = round((weight / (height / 100).pow(2)) * 10) / 10.0

fun calculateCalories(minutes: Long, equipment: String, level: String, weight: Double): Double {
    val met: Double = when (equipment) {
        "Barbell", "Dumbell" -> {
            when (level) {
                "Ringan" -> 3.0
                "Sedang" -> 4.0
                "Tinggi" -> 6.0
                else -> 0.0
            }
        }
        "Gym-ball" -> {
            when (level) {
                "Ringan" -> 2.5
                "Sedang" -> 3.0
                "Tinggi" -> 5.0
                else -> 0.0
            }
        }
        "Kattle-ball", "Roller-abs" -> {
            when (level) {
                "Ringan" -> 4.0
                "Sedang" -> 5.0
                "Tinggi" -> 7.0
                else -> 0.0
            }
        }
        "Leg-press" -> {
            when (level) {
                "Ringan" -> 3.5
                "Sedang" -> 4.0
                "Tinggi" -> 6.0
                else -> 0.0
            }
        }
        "Punching-bag" -> {
            when (level) {
                "Ringan" -> 5.5
                "Sedang" -> 6.0
                "Tinggi" -> 8.0
                else -> 0.0
            }
        }
        "Statis-bicycle" -> {
            when (level) {
                "Ringan" -> 3.5
                "Sedang" -> 4.0
                "Tinggi" -> 7.0
                else -> 0.0
            }
        }
        "Step" -> {
            when (level) {
                "Ringan" -> 4.5
                "Sedang" -> 5.0
                "Tinggi" -> 8.0
                else -> 0.0
            }
        }
        "Treadmill" -> {
            when (level) {
                "Ringan" -> 3.0
                "Sedang" -> 5.0
                "Tinggi" -> 9.0
                else -> 0.0
            }
        }
        else -> 0.0
    }
    return round((minutes * (met * 3.5 * weight) / 200.0) * 10.0) / 10.0
}
