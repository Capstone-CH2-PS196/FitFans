package com.capstonech2.fitfans.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.capstonech2.fitfans.data.model.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM tb_collection ORDER BY id ASC")
    fun getAllCollection() : LiveData<List<Collection>>
}