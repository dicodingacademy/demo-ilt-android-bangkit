package com.dicoding.parsingjson.di

import com.dicoding.parsingjson.network.ApiConfig
import com.dicoding.parsingjson.network.UserRepository

// TODO : [5] Create Simple Injection to provide Repository
object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}