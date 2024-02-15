package com.dicoding.picodiploma.mynoteapps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// TODO : [3] Create Database

abstract class NoteRoomDatabase {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NoteRoomDatabase {

            return INSTANCE as NoteRoomDatabase
        }
    }
}