package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentAndUniversity
import com.dicoding.mystudentdata.database.StudentAndUniversityWithCourse
import com.dicoding.mystudentdata.database.StudentWithCourse
import com.dicoding.mystudentdata.database.UniversityAndStudent
import kotlinx.coroutines.launch

class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    // TODO [1] Delete insertAllData function from MainViewModel
    init {
        insertAllData()
    }
    private fun insertAllData() = viewModelScope.launch {
        studentRepository.insertAllData()
    }

    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentRepository.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentRepository.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentRepository.getAllStudentWithCourse()

    fun getAllStudentAndUniversityWithCourse(): LiveData<List<StudentAndUniversityWithCourse>> = studentRepository.getAllStudentAndUniversityWithCourse()
}

class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}