package com.dicoding.picodiploma.mynoteapps.database

import androidx.lifecycle.LiveData
import androidx.room.*

// TODO : [2] Create Dao
interface NoteDao {

    fun insert(note: Note)

    fun update(note: Note)

    fun delete(note: Note)

    fun getAllNotes(): LiveData<List<Note>>
}