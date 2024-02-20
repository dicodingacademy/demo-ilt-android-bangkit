package com.dicoding.parsingjson.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.parsingjson.di.Injection
import com.dicoding.parsingjson.network.UserRepository

// TODO : [6] Create ViewModel
class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    val users = userRepository.users
    val isLoading = userRepository.isLoading
    val isError = userRepository.isError

    fun getUser() { userRepository.getUser() }

    // TODO: [7] Create ViewModel Factory
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return MainViewModel(Injection.provideRepository()) as T
            }
        }
    }
}

