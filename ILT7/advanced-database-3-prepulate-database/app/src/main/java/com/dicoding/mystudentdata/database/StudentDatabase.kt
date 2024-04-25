package com.dicoding.mystudentdata.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dicoding.mystudentdata.helper.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class],
    // TODO [6] To resolve the aforementioned error, upgrade the database version to 2 in StudentDatabase.
    version = 1,
    // TODO [2] The primary prerequisite for automated migration is enabling database schema export.
    exportSchema = false
    // TODO [8] The next step involves adding AutoMigration as before. However, there's a slight difference here. When renaming a field, the system doesn't automatically detect the change. Instead, it treats the new name as a new field and removes the old one. To address this, we need to add a specification.
)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context, applicationScope: CoroutineScope): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration()
//                        .createFromAsset("student_database.db")
                        .addCallback(object :Callback(){
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                INSTANCE?.let { database ->
                                    applicationScope.launch {
                                        val studentDao = database.studentDao()
                                        studentDao.insertStudent(InitialDataSource.getStudents())
                                        studentDao.insertUniversity(InitialDataSource.getUniversities())
                                        studentDao.insertCourse(InitialDataSource.getCourses())
                                        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())                                }
                                }
                            }
                        })
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}