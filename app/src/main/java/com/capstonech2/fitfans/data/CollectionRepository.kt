package com.capstonech2.fitfans.data

import android.media.Image
import androidx.lifecycle.LiveData
import com.capstonech2.fitfans.data.local.FitfansDatabase
import com.capstonech2.fitfans.data.model.Collection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollectionRepository(private val database: FitfansDatabase) {

    fun getAllCollection() : LiveData<List<Collection>> =
        database.collectionDao().getAllCollection()

    fun getCollectionById(colId: Int) : LiveData<Collection> =
        database.collectionDao().getCollectionById(colId)

    fun getCollectionByImage(image: String) : LiveData<Collection> =
        database.collectionDao().getCollectionByImage(image)

    suspend fun insertCollection(collection: Collection) = withContext(Dispatchers.IO){
        database.collectionDao().insertCollection(collection)
    }

    suspend fun deleteCollection(image: String) = withContext(Dispatchers.IO){
        database.collectionDao().deleteCollection(image)
    }
}