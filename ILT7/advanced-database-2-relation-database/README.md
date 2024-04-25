[1] Delete insertAllData function from MainViewModel
```
init {
    insertAllData()
}
private fun insertAllData() = viewModelScope.launch {
      studentRepository.insertAllData()
}
```

[2] Delete insertAllData function from StudentRepository
```
    suspend fun insertAllData() {
        studentDao.insertStudent(InitialDataSource.getStudents())
        studentDao.insertUniversity(InitialDataSource.getUniversities())
        studentDao.insertCourse(InitialDataSource.getCourses())
        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
    }
```

[3] Add an applicationScope parameter and an addCallback function to the getDatabase function in StudentDatabase.
```
        @JvmStatic
        fun getDatabase(context: Context, applicationScope: CoroutineScope): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration()
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
```

[4] Add an argument for the applicationScope parameter in MyApplication.
```
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { StudentDatabase.getDatabase(this, applicationScope) }
```

[5] If you want to import data using a database file, you can use the following code.
```
.createFromAsset("student_database.db")
```