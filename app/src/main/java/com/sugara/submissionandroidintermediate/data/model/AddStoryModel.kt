package com.sugara.submissionandroidintermediate.data.model

import com.google.gson.annotations.SerializedName

data class AddStoryModel(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
