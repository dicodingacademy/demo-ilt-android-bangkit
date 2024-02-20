package com.dicoding.parsingjson.di

import com.dicoding.parsingjson.network.ApiConfig
import com.dicoding.parsingjson.network.UserRepository

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}