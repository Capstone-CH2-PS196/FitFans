package com.capstonech2.fitfans.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recommendation(
    val level: String,
    val set: String,
    val time: Long
) : Parcelable