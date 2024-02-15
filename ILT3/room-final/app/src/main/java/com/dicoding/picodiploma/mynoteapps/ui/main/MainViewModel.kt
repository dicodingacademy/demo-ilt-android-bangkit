package com.dicoding.picodiploma.mynoteapps.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mynoteapps.database.Note
import com.dicoding.picodiploma.mynoteapps.repository.NoteRepository

class MainViewModel(application: Application) : ViewModel() {
    // TODO - [5] Call Repository
    private val mNoteRepository: NoteRepository = NoteRepository(application)

    // TODO - [6] Get All Notes from Room Database
    fun getAllNotes(): LiveData<List<Note>> = mNoteRepository.getAllNotes()
}