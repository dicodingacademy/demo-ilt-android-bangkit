package com.dicoding.mystudentdata.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: List<Student>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUniversity(university: List<University>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: List<Course>)

    @Query("SELECT * from student")
    fun getAllStudent(): LiveData<List<Student>>

    // TODO [2] Add a function to retrieve relational data from students and universities.

    // TODO [8] Add a function to retrieve relational data from universities and students.

    // TODO [15] Add functions to insert and retrieve Student and Course data.
}