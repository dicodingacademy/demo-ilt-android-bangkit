package com.dicoding.picodiploma.mynoteapps.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.mynoteapps.database.Note
import com.dicoding.picodiploma.mynoteapps.database.NoteDao
import com.dicoding.picodiploma.mynoteapps.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO : [4] Create Repository to Connect Room Database
class NoteRepository private constructor(
    private var noteDao: NoteDao,
    private val executorService: ExecutorService
) {

    // TODO : [5] Create a function to manage data from the room database.
    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    fun insert(note: Note) {
        executorService.execute { noteDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { noteDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { noteDao.update(note) }
    }

    // TODO : [6] Create an instance of Note Repository as a singleton.
    companion object {
        @Volatile
        private var instance: NoteRepository? = null
        fun getInstance(
            noteDao: NoteDao,
            executorService: ExecutorService
        ): NoteRepository = instance ?: synchronized(this) {
            instance ?: NoteRepository(noteDao, executorService)
        }.also { instance = it }
    }
}