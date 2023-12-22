package com.capstonech2.fitfans.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.capstonech2.fitfans.utils.StringListConverter

@Entity("tb_collection")
data class Collection (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val image: String,

    @Embedded
    val timerRecommendation: TimerRecommendationEntity,

    val toolName: String,

    @TypeConverters(StringListConverter::class)
    val howToUse: List<String>,

    val toolDescription: String
)

data class TimerRecommendationEntity(
    val expert: Long,
    val ideal: Long,
    val beginner: Long
)