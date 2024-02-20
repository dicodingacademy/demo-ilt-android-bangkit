package com.dicoding.parsingjson.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.parsingjson.di.Injection
import com.dicoding.parsingjson.network.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    // TODO : [4] Adjust your function
    fun getUser() = userRepository.getUser()

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

