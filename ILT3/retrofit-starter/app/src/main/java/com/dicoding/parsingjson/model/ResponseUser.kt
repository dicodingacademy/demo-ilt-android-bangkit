package com.dicoding.parsingjson.model

// TODO : [1] Add Response User
data class ResponseUser(

	val page: Int? = null,

	val perPage: Int? = null,

	val total: Int? = null,

	val totalPages: Int? = null,

	val data: List<DataItem>? = null
)