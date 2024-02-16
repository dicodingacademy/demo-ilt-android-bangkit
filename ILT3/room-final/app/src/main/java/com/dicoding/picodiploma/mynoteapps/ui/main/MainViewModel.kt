package com.dicoding.picodiploma.mynoteapps.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mynoteapps.database.Note
import com.dicoding.picodiploma.mynoteapps.repository.NoteRepository

class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    // TODO - [8] Get All Notes from Room Database
    fun getAllNotes(): LiveData<List<Note>> = noteRepository.getAllNotes()
}