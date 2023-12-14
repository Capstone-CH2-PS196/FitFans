package com.capstonech2.fitfans.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_note")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val title: String,
    val description: String,
    var isChecked: Boolean = false
)