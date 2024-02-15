package com.dicoding.picodiploma.mynoteapps.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.mynoteapps.database.Note
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO : [4] Create Repository to Connect Room Database
class NoteRepository(application: Application) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {

    }

    fun getAllNotes(): LiveData<List<Note>> = MutableLiveData(emptyList())

    fun insert(note: Note) {
        executorService.execute {  }
    }

    fun delete(note: Note) {
        executorService.execute {  }
    }

    fun update(note: Note) {
        executorService.execute {  }
    }
}