package com.capstonech2.fitfans.utils

import kotlin.math.pow
import kotlin.math.round

fun String.capitalizeFirstLetter(): String {
    return if (this.isEmpty()) {
        this
    } else {
        this[0].uppercaseChar() + substring(1)
    }
}