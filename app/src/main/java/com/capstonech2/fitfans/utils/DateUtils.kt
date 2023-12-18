package com.capstonech2.fitfans.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(date)
}

fun formatDateWithDay(date: String?): String {
    return date?.let { getDayFromDate(it) }?.let { "$it, $date" } ?: ""
}

fun getDayFromDate(dateString: String): String {
    return try {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        dayOfWeek ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}