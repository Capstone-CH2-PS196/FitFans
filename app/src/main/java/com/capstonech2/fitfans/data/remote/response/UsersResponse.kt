package com.capstonech2.fitfans.data.remote.response

import com.google.gson.annotations.SerializedName

data class UsersResponse(

	@field:SerializedName("UsersResponse")
	val usersResponse: List<UsersResponseItem>
)

data class UsersResponseItem(

	@field:SerializedName("full_name")
	val fullName: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("weight")
	val weight: Double,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("height")
	val height: Double,

	@field:SerializedName("image")
	val image: String
)
