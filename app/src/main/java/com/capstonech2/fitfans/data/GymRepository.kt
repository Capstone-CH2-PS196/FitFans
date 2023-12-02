package com.capstonech2.fitfans.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstonech2.fitfans.data.remote.service.ApiService
import com.capstonech2.fitfans.data.remote.response.ErrorResponse
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.utils.State
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class GymRepository(private val apiService: ApiService) {
    fun insertUser(data: UsersResponseItem) =
        liveData {
            emit(State.Loading)
            try {
                val response = apiService.insertUser(data)
                emit(State.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                emit(State.Error(errorResponse.error))
            }
        }

    fun getUserByEmail(email: String): LiveData<State<List<UsersResponseItem>>> =
        liveData(Dispatchers.IO) {
            emit(State.Loading)
            try {
                val response = apiService.getUserByEmail(email)
                emit(State.Success(response))
            } catch (e: HttpException) {
                val errorMessage = e.localizedMessage ?: "Unknown error occurred"
                emit(State.Error(errorMessage))
            }
        }
}