package com.capstonech2.fitfans.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Predicts(
    val image: String,
    val timerRecommendation: TimerRecommendation,
    val toolName: String,
    val howToUse: List<String>,
    val toolDescription: String
) : Parcelable

@Parcelize
data class TimerRecommendation(
    val expert: Int,
    val ideal: Int,
    val beginner: Int
) : Parcelable