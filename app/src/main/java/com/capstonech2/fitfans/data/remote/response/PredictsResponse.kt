package com.capstonech2.fitfans.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictsResponse(

	@field:SerializedName("tool_name")
	val toolName: String,

	@field:SerializedName("how_to_use")
	val howToUse: List<String>,

	@field:SerializedName("tool_description")
	val toolDescription: String
)
