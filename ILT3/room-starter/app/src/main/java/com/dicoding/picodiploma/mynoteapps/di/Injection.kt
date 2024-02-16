package com.dicoding.picodiploma.mynoteapps.di

import android.content.Context
import com.dicoding.picodiploma.mynoteapps.database.NoteRoomDatabase
import com.dicoding.picodiploma.mynoteapps.repository.NoteRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO : [7] Create an Injection to provide a repository
object Injection {
    fun provideRepository(context: Context): NoteRepository {
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        return NoteRepository.getInstance(executorService)
    }
}