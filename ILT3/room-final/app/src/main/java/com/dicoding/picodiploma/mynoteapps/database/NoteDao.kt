package com.dicoding.picodiploma.mynoteapps.database

import androidx.lifecycle.LiveData
import androidx.room.*

// TODO : [2] Create Dao
@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * from note ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>
}