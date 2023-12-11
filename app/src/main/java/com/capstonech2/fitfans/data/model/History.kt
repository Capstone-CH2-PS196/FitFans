package com.capstonech2.fitfans.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_history")
data class History (
    @PrimaryKey(autoGenerate = true)
    val id : Int
)