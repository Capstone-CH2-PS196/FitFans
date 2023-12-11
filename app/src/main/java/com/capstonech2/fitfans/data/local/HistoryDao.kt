package com.capstonech2.fitfans.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.capstonech2.fitfans.data.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM tb_history ORDER BY id ASC")
    fun getAllHistory() : LiveData<List<History>>
}