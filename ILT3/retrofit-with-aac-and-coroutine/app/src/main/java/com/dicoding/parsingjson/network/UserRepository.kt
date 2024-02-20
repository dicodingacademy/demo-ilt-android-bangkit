package com.dicoding.parsingjson.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.parsingjson.model.DataItem

class UserRepository private constructor(
    private val apiService: ApiService
) {

    // TODO : [2] Change getUser using Coroutine
    fun getUser(): LiveData<Result<List<DataItem>>> = liveData {
        emit(Result.Loading)
        try {

            val response = apiService.getListUsersUsingCoroutine("1")
            if (response.data != null) {
                emit(Result.Success(response.data))
            } else {
                emit(Result.Error("Data Item is empty."))
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}

// TODO : [3] Create SingleEvent for LiveData
sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}