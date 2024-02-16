package com.dicoding.picodiploma.mynoteapps.database

import android.content.Context
// TODO : [3] Create Room Database
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