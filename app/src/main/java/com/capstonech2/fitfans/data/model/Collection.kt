package com.capstonech2.fitfans.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tb_collection")
data class Collection (
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val name: String,
)