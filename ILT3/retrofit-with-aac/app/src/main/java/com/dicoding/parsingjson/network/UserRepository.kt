package com.dicoding.parsingjson.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dicoding.parsingjson.model.DataItem
import com.dicoding.parsingjson.model.ResponseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO : [1] Create Repository to Manage Network Data Source.
class UserRepository private constructor(
    private val apiService: ApiService
) {

    private val _users = MutableLiveData<List<DataItem>>()
    val users: LiveData<List<DataItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    // TODO : [2] Move getUser from Activity to Repository and give some improvement
    fun getUser() {
        _isLoading.value = true
        val client = apiService.getListUsers("1")

        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _users.value = response.body()?.data as List<DataItem>
                    } else {
                        _isError.value = "onFailure: ${response.message()}"
                    }
                } else {
                    _isError.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                _isLoading.value = false
                _isError.value = "onFailure: ${t.message.toString()}"
                t.printStackTrace()
            }
        })
    }

    // TODO : [3] Create Singleton Initialization for this Class Repository
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