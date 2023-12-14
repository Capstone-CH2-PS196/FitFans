package com.capstonech2.fitfans.data.remote.service

import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.response.AddUsersResponse
import com.capstonech2.fitfans.data.remote.response.PredictsResponse
import com.capstonech2.fitfans.data.remote.response.UpdateUserResponse
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface ApiService {

    @POST("users")
    suspend fun insertUser(
        @Body user: User
    ): AddUsersResponse

    @GET("users")
    suspend fun getUserByEmail(
        @Query("user_email") email: String
    ): List<UsersResponseItem>

    @PUT("users/{email}")
    suspend fun updateUserDataByEmail(
        @Path("email") email: String,
        @Body user: User
    ): UpdateUserResponse

    @POST("predicts")
    suspend fun predictEquipment(
        @Query("file") file : File
    ): PredictsResponse
}