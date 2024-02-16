package com.dicoding.parsingjson.model

import com.google.gson.annotations.SerializedName

// TODO : [1] Add Response User
data class ResponseUser(
	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>? = null
)