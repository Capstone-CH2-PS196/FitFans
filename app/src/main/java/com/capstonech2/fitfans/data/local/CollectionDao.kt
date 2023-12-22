package com.capstonech2.fitfans.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstonech2.fitfans.data.model.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM tb_collection")
    fun getAllCollection(): LiveData<List<Collection>>

    @Query("SELECT * FROM tb_collection WHERE image = :image")
    fun getCollectionByImage(image: String): LiveData<Collection>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(collection: Collection)

    @Query("DELETE FROM tb_collection WHERE image = :image")
    suspend fun deleteCollection(image: String)
}