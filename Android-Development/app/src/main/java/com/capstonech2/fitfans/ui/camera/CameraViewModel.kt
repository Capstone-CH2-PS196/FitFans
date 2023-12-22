package com.capstonech2.fitfans.ui.camera

import androidx.lifecycle.ViewModel
import com.capstonech2.fitfans.data.PredictsRepository
import java.io.File

class CameraViewModel(private val repository: PredictsRepository) : ViewModel() {
    fun uploadImage(file: File) = repository.detectImage(file)
}