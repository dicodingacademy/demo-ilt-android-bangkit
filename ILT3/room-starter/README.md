# academies

[0] Add Depdendency for LiveData, ViewModel, and Room Database
```
// Livedata
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
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

[4] Create Repository to Connect Room Database
```
class NoteRepository(application: Application) {
private val mNotesDao: NoteDao
private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNotesDao = db.noteDao()
    }

    fun getAllNotes(): LiveData<List<Note>> = mNotesDao.getAllNotes()

    fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }
}
```

[5] Call Repository & [6] Get All Notes from Room Database
```
class MainViewModel(application: Application) : ViewModel() {
// TODO - 
private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes(): LiveData<List<Note>> = mNoteRepository.getAllNotes()
}
```

[7] Observe notes from ViewModel & [8] Show notes to RecyclerView
```
mainViewModel.getAllNotes().observe(this) { noteList ->
    if (noteList != null) {
        adapter.setListNotes(noteList)
    }
}
```

[9] Call Another Function form Room Dao
```
private val noteRepository: NoteRepository = NoteRepository(application)

fun insert(note: Note) {
    noteRepository.insert(note)
}

fun update(note: Note) {
    noteRepository.update(note)
}

fun delete(note: Note) {
    noteRepository.delete(note)
}
```

[10] Use isEdit to determine which function will be called, whether insert or update.
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

[11] Call the delete function to delete the note.
```
noteAddUpdateViewModel.delete(note as Note)
```
