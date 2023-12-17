package com.capstonech2.fitfans.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_exercise")
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    val exeId: Int,
    val exeToolName: String,
    val exeCal: Double,
    val exeTime: Long,
    val historyID: Int,
)