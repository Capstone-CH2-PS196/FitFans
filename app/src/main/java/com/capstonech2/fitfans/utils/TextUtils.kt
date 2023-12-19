package com.capstonech2.fitfans.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.capitalizeFirstLetter(): String {
    return if (this.isEmpty()) {
        this
    } else {
        this[0].uppercaseChar() + substring(1)
    }
}

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<String>): String {
        return Gson().toJson(value)
    }
}

fun TextView.show(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun RecyclerView.show(state: Boolean){
    visibility = if (state) View.VISIBLE else View.GONE
}