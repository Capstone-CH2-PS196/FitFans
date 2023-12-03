package com.capstonech2.fitfans.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.service.ApiService
import com.capstonech2.fitfans.data.remote.response.UsersResponse
import com.capstonech2.fitfans.utils.State
import kotlinx.coroutines.Dispatchers

class GymRepository(private val apiService: ApiService) {
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

    fun getUserByEmail(email: String): LiveData<State<UsersResponse>> =
        liveData(Dispatchers.IO) {
            emit(State.Loading)
            try {
                val response = apiService.getUserByEmail(email)
                emit(State.Success(response))
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Unknown error occurred"
                emit(State.Error(errorMessage))
            }
        }
}