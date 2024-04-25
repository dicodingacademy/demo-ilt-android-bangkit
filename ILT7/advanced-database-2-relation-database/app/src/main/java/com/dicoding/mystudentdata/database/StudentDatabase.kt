package com.dicoding.mystudentdata.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        // TODO [3] Add an applicationScope parameter and an addCallback function to the getDatabase function in StudentDatabase.
        @JvmStatic
        fun getDatabase(context: Context): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration()
                        // TODO [5] If you want to import data using a database file, you can call createFromAsset.
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}