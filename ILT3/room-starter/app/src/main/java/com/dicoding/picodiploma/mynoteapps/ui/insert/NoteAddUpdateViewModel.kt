package com.dicoding.picodiploma.mynoteapps.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.mynoteapps.database.Note
import com.dicoding.picodiploma.mynoteapps.repository.NoteRepository

class NoteAddUpdateViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    // TODO - [9] Call Another Function form Room Dao
    fun insert(note: Note) {
    }

    fun update(note: Note) {
    }

    fun delete(note: Note) {
    }

}