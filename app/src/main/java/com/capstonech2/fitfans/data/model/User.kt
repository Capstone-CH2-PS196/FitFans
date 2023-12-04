package com.capstonech2.fitfans.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val full_name: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val email: String,
    val image: String? = null
) : Parcelable
