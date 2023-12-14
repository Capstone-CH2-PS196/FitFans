package com.capstonech2.fitfans.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictsResponse(

	@field:SerializedName("timer_recomendation")
	val timerRecomendation: TimerRecomendation,

	@field:SerializedName("tool_name")
	val toolName: String,

	@field:SerializedName("how_to_use")
	val howToUse: List<String>,

	@field:SerializedName("tool_description")
	val toolDescription: String
)

data class TimerRecomendation(

	@field:SerializedName("expert")
	val expert: Int,

	@field:SerializedName("ideal")
	val ideal: Int,

	@field:SerializedName("beginner")
	val beginner: Int
)
