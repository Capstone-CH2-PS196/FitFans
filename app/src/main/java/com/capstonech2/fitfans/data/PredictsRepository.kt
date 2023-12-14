package com.capstonech2.fitfans.data

import androidx.lifecycle.liveData
import com.capstonech2.fitfans.data.remote.service.ModelApiService
import com.capstonech2.fitfans.utils.State
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PredictsRepository(private val apiService: ModelApiService) {
    fun detectImage(imageFile: File) = liveData {
            emit(State.Loading)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = apiService.uploadImage(multipartBody)
                emit(State.Success(successResponse))
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Unknown error occurred"
                emit(State.Error(errorMessage))
            }
        }
}