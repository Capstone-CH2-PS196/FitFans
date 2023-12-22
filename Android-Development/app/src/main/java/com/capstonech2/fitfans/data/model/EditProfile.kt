package com.capstonech2.fitfans.data.model

data class EditProfile(
    val full_name: String,
    val gender: String,
    val age: Int,
    val weight: Double,
    val height: Double,
    val email: String,
    val image: String? = null,
)
