## Many-to-One Relationship

[1] Add a relationship between student and university.
```
data class StudentAndUniversity(
    @Embedded
    val student: Student,
    @Relation(
        parentColumn = "univId",
        entityColumn = "universityId"
    )
    val university: University? = null
)
```

[2] Add a function to retrieve relational data from students and universities.
```
@Transaction
@Query("SELECT * from student")
fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>>
```

[3] Call the getAllStudentAndUniversity function in the StudentRepository.
```    
fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()
```

[4] Add the getAllStudentAndUniversity function to the MainViewModel.
```    
fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentRepository.getAllStudentAndUniversity()
```

[5] Create an Adapter class to hold detailed information from Student and University.
```
class StudentAndUniversityAdapter :
ListAdapter<StudentAndUniversity, StudentAndUniversityAdapter.WordViewHolder>(WordsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentAndUniversity) {
            binding.tvItemName.text = data.student.name
            binding.tvItemUniversity.text = data.university?.name
            binding.tvItemUniversity.visibility = View.VISIBLE        }
    }

    class WordsComparator : DiffUtil.ItemCallback<StudentAndUniversity>() {
        override fun areItemsTheSame(oldItem: StudentAndUniversity, newItem: StudentAndUniversity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StudentAndUniversity, newItem: StudentAndUniversity): Boolean {
            return oldItem.student.name == newItem.student.name
        }
    }
}
```

[6] Display relational database data student and university in a recyclerview.
```
    private fun getStudentAndUniversity() {
        val adapter = StudentAndUniversityAdapter()
        binding.rvStudent.adapter = adapter
        mainViewModel.getAllStudentAndUniversity().observe(this) {
            adapter.submitList(it)
            Log.d(TAG, "getStudentAndUniversity: $it")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
```

## One-to-Many Relationship

[7] Add a relationship between university and student.
```
data class UniversityAndStudent(
    @Embedded
    val university: University,
    @Relation(
        parentColumn = "universityId",
        entityColumn = "univId"
    )
    val student: List<Student>
)
```

[8] Add a function to retrieve relational data from universities and students.
```
@Transaction
@Query("SELECT * from university")
fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>>
```

[9] Call the getAllUniversityAndStudent function in the StudentRepository.
```    
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentDao.getAllUniversityAndStudent()
```

[10] Add the getAllUniversityAndStudent function to the MainViewModel.
```    
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentRepository.getAllUniversityAndStudent()
```

[11] Create an Adapter class to hold detailed information from University and Student.
```
class UniversityAndStudentAdapter :
    ListAdapter<UniversityAndStudent, UniversityAndStudentAdapter.WordViewHolder>(
        WordsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UniversityAndStudent) {
            val arrayName = arrayListOf<String>()
            data.student.forEach {
                arrayName.add(it.name)
            }
            binding.tvItemName.text = arrayName.joinToString(separator = ", ")
            binding.tvItemUniversity.text = data.university.name
            binding.tvItemUniversity.visibility = View.VISIBLE
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<UniversityAndStudent>() {
        override fun areItemsTheSame(oldItem: UniversityAndStudent, newItem: UniversityAndStudent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UniversityAndStudent, newItem: UniversityAndStudent): Boolean {
            return oldItem.university.name == newItem.university.name
        }
    }
}
```

[12] Display relational database data university and student in a recyclerview.
```
    private fun getStudentAndUniversity() {
        val adapter = StudentAndUniversityAdapter()
        binding.rvStudent.adapter = adapter
        mainViewModel.getAllStudentAndUniversity().observe(this) {
            adapter.submitList(it)
            Log.d(TAG, "getStudentAndUniversity: $it")
        }
    }
```

## Many-to-Many Relationship

[13] Add a relationship between Student and Course.
```
@Entity(primaryKeys = ["sId", "cId"])
data class CourseStudentCrossRef(
    val sId: Int,
    @ColumnInfo(index = true)
    val cId: Int,
)
data class StudentWithCourse(
    @Embedded
    val student: Student,
    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)
```

[14] Add a table to the Student Database configuration.
```
@Database(entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {
  ...
}
```

[15] Add functions to insert and retrieve Student and Course data.
```
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseStudentCrossRef(courseStudentCrossRef: List<CourseStudentCrossRef>)

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>>
```
[16] Add a getCourseStudentRelation function to insert many-to-many relationship data when the application starts for the first time.
```
    fun getCourseStudentRelation(): List<CourseStudentCrossRef> {
        return listOf(
            CourseStudentCrossRef(1, 1),
            CourseStudentCrossRef(1, 2),
            CourseStudentCrossRef(2, 2),
            CourseStudentCrossRef(2, 5),
            CourseStudentCrossRef(3, 3),
            CourseStudentCrossRef(4, 3),
            CourseStudentCrossRef(4, 4),
            CourseStudentCrossRef(5, 4),
            CourseStudentCrossRef(6, 3),
            CourseStudentCrossRef(6, 4),
        )
    }

```

[17] Call the getCourseStudentRelation function to insert and retrieve student and course data within the StudentRepository.
```
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentDao.getAllStudentWithCourse()

    suspend fun insertAllData() {
        ...
        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
    }
```

[18] Add the getAllStudentWithCourse function to the MainViewModel.
```    
   fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentRepository.getAllStudentWithCourse()
```

[19] Create an Adapter class to hold detailed information from Students and Courses.
```
class UniversityAndStudentAdapter :
    ListAdapter<UniversityAndStudent, UniversityAndStudentAdapter.WordViewHolder>(
        WordsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UniversityAndStudent) {
            val arrayName = arrayListOf<String>()
            data.student.forEach {
                arrayName.add(it.name)
            }
            binding.tvItemName.text = arrayName.joinToString(separator = ", ")
            binding.tvItemUniversity.text = data.university.name
            binding.tvItemUniversity.visibility = View.VISIBLE
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<UniversityAndStudent>() {
        override fun areItemsTheSame(oldItem: UniversityAndStudent, newItem: UniversityAndStudent): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UniversityAndStudent, newItem: UniversityAndStudent): Boolean {
            return oldItem.university.name == newItem.university.name
        }
    }
}
```

[20] Display relational database data university and student in a recyclerview.
```
    private fun getStudentWithCourse() {
        val adapter = StudentWithCourseAdapter()
        binding.rvStudent.adapter = adapter
        mainViewModel.getAllStudentWithCourse().observe(this) {
            Log.d(TAG, "getStudentWithCourse: $it")
            adapter.submitList(it)
        }
    }

```

## StudentAndUniversityWithCourse
[21] If you want to display the relationships between universities, students, and courses, you can create a StudentAndUniversityWithCourse class.
```
data class StudentAndUniversityWithCourse(
    @Embedded
    val studentAndUniversity: StudentAndUniversity,
    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)
```

[22] You can create new adapter to handle Student and University with Course
```
class StudentAndUniversityWithCourseAdapter :
    ListAdapter<StudentAndUniversityWithCourse, StudentAndUniversityWithCourseAdapter.WordViewHolder>(
        WordsComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WordViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentAndUniversityWithCourse) {
            binding.tvItemUniversity.text = data.studentAndUniversity.university?.name
            binding.tvItemUniversity.visibility = View.VISIBLE
            binding.tvItemName.text = data.studentAndUniversity.student.name
            val arrayCourse = arrayListOf<String>()
            data.course.forEach {
                arrayCourse.add(it.name)
            }
            binding.tvItemCourse.text = arrayCourse.joinToString(separator = ", ")
            binding.tvItemCourse.visibility = View.VISIBLE
        }
    }

    class WordsComparator : DiffUtil.ItemCallback<StudentAndUniversityWithCourse>() {
        override fun areItemsTheSame(
            oldItem: StudentAndUniversityWithCourse,
            newItem: StudentAndUniversityWithCourse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: StudentAndUniversityWithCourse,
            newItem: StudentAndUniversityWithCourse
        ): Boolean {
            return oldItem.studentAndUniversity.student.name == newItem.studentAndUniversity.student.name
        }
    }
}
```
