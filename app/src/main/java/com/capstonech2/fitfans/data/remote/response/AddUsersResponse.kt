package com.capstonech2.fitfans.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddUsersResponse(

	@field:SerializedName("fieldCount")
	val fieldCount: Int,

	@field:SerializedName("serverStatus")
	val serverStatus: Int,

	@field:SerializedName("changedRows")
	val changedRows: Int,

	@field:SerializedName("affectedRows")
	val affectedRows: Int,

	@field:SerializedName("warningStatus")
	val warningStatus: Int,

	@field:SerializedName("insertId")
	val insertId: Int,

	@field:SerializedName("info")
	val info: String
)

data class ErrorResponse(

	@field:SerializedName("error")
	val error: String
)
