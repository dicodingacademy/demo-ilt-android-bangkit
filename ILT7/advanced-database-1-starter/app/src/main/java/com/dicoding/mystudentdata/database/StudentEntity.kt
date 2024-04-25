package com.dicoding.mystudentdata.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey
    val studentId: Int,
    val name: String,
    val univId: Int,
)

@Entity
data class University(
    @PrimaryKey
    val universityId: Int,
    val name: String,
)

@Entity
data class Course(
    @PrimaryKey
    val courseId: Int,
    val name: String,
)

// TODO [1] Add a relationship between student and university.

// TODO [7] Add a relationship between university and student.

// TODO [21] If you want to display the relationships between universities, students, and courses, you can create a StudentAndUniversityWithCourse class.
