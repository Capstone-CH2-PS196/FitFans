package com.capstonech2.fitfans.data

import androidx.lifecycle.liveData
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.service.ApiService
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.utils.State

class GymRepository(private val apiService: ApiService) {

    suspend fun getUserByEmail(email: String):  List<UsersResponseItem> = apiService.getUserByEmail(email)

    fun insertUser(data: User) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.insertUser(data)
            emit(State.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Unknown error occurred"
            emit(State.Error(errorMessage))
        }
    }

    fun updateUserByEmail(email: String, data: User) = liveData {
        emit(State.Loading)
        try {
            val response = apiService.updateUserDataByEmail(email, data)
            emit(State.Success(response))
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Unknown error occurred"
            emit(State.Error(errorMessage))
        }
    }
}