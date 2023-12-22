package com.capstonech2.fitfans.data.remote.service

import com.capstonech2.fitfans.data.remote.response.PredictsResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ModelApiService {
    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): PredictsResponse
}