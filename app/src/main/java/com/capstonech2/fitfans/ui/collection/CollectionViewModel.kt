package com.capstonech2.fitfans.ui.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonech2.fitfans.data.CollectionRepository
import com.capstonech2.fitfans.data.model.Collection
import kotlinx.coroutines.launch

class CollectionViewModel(private val repository: CollectionRepository) : ViewModel(){
    fun getAllCollection() = repository.getAllCollection()

    fun getCollectionById(colId: Int) = repository.getCollectionById(colId)

    fun getCollectionByImage(image: String) = repository.getCollectionByImage(image)

    fun insertResult(collection: Collection) = viewModelScope.launch {
        repository.insertCollection(collection)
    }

    fun deleteCollection(image: String) = viewModelScope.launch {
        repository.deleteCollection(image)
    }
}