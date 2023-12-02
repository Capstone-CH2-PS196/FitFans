package com.capstonech2.fitfans.data.remote.service

import com.capstonech2.fitfans.data.remote.response.AddUsersResponse
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("users")
    suspend fun insertUser(
        @Body user: UsersResponseItem
    ): AddUsersResponse

    @GET("users")
    suspend fun getUserByEmail(
        @Query("user_email") email: String
    ): List<UsersResponseItem>
}