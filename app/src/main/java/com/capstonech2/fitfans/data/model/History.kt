package com.capstonech2.fitfans.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "tb_history")
data class History (
    @PrimaryKey(autoGenerate = true)
    val hisId : Int = 0,
    val date: String,
)

@Entity(tableName = "tb_exercise")
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    val exeId: Int = 0,
    val exeToolName: String,
    val exeCal: Double,
    val exeTime: Long,
    val historyId: Int,
)

data class HistoryAndExercise(
    @Embedded
    val exercise : Exercise,

    @Relation(
        parentColumn = "historyId",
        entityColumn = "hisId"
    )
    val history: History? = null
)