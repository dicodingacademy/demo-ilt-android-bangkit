# academies

[0] Add Depdendency for LiveData, ViewModel, and Room Database
```
//room
implementation("androidx.room:room-runtime:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")
```

[1] Create Entity
```
@Entity(tableName = "Note")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null
) : Parcelable
```

[2] Create Dao
```
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
```

[3] Create Database
```
@Database(entities = [Note::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NoteRoomDatabase {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            NoteRoomDatabase::class.java, "note_database")
                            .build()
                }
            }
            return INSTANCE as NoteRoomDatabase
        }
    }
}
```

[4] Create Repository to Connect Room Database & [5] Create a function to manage data from the room database.
```
class NoteRepository private constructor(
    private var noteDao: NoteDao,
    private val executorService: ExecutorService
) {

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    fun insert(note: Note) {
        executorService.execute { noteDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { noteDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { noteDao.update(note) }
    }
}
```

[6] Create an instance of Note Repository as a singleton.
```
    companion object {
        @Volatile
        private var instance: NoteRepository? = null
        fun getInstance(
            noteDao: NoteDao,
            executorService: ExecutorService
        ): NoteRepository = instance ?: synchronized(this) {
            instance ?: NoteRepository(noteDao, executorService)
        }.also { instance = it }
    }
```

[7] Create an Injection to provide a repository
```
object Injection {
    fun provideRepository(context: Context): NoteRepository {
        val database = NoteRoomDatabase.getDatabase(context)
        val dao = database.noteDao()
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        return NoteRepository.getInstance(dao, executorService)
    }
}
```

[8] Get All Notes from Room Database
```
class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    fun getAllNotes(): LiveData<List<Note>> = noteRepository.getAllNotes()
}
```

[9] Call Another Function form Room Dao
```
class NoteAddUpdateViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    fun insert(note: Note) {
        noteRepository.insert(note)
    }

    fun update(note: Note) {
        noteRepository.update(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

}
```

[10] Create View Model Factory to provide ViewModel
```
class ViewModelFactory private constructor(private val noteRepository: NoteRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(noteRepository) as T
        } else if (modelClass.isAssignableFrom(NoteAddUpdateViewModel::class.java)) {
            return NoteAddUpdateViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
```

[11] Instance ViewModelFactory to obtain ViewModel
```
    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
```

[12] Observe notes from ViewModel & [13] Show notes to RecyclerView
```
val mainViewModel = obtainViewModel(this@MainActivity)
    mainViewModel.getAllNotes().observe(this) { noteList ->
    if (noteList != null) {
        adapter.setListNotes(noteList)
    }
}
```

[14] Instance ViewModelFactory to obtain ViewModel
```
private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
    val factory = ViewModelFactory.getInstance(activity.application)
    return ViewModelProvider(activity, factory).get(NoteAddUpdateViewModel::class.java)
}

noteAddUpdateViewModel = obtainViewModel(this@NoteAddUpdateActivity)
```

[15] Use isEdit to determine which function will be called, whether insert or update.
```
if (isEdit) {
    noteAddUpdateViewModel.update(note as Note)
    showToast(getString(R.string.changed))
} else {
    note.let { note ->
        note?.date = DateHelper.getCurrentDate()
    }
    noteAddUpdateViewModel.insert(note as Note)
    showToast(getString(R.string.added))
}
```

[16] Call the delete function to delete the note.
```
noteAddUpdateViewModel.delete(note as Note)
```
