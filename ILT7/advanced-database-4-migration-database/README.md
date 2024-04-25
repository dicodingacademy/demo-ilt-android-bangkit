[1] Create a new package named helper. Then, inside that package, create a new class named SortType.

```
enum class SortType {
   ASCENDING,
   DESCENDING,
   RANDOM
}
```

[2] Create a new file named SortUtils in the helper package. Once created, add a method to create a query.

```
object SortUtils {
   fun getSortedQuery(sortType: SortType): SimpleSQLiteQuery {
       val simpleQuery = StringBuilder().append("SELECT * FROM student ")
       when (sortType) {
           SortType.ASCENDING -> {
               simpleQuery.append("ORDER BY name ASC")
           }
           SortType.DESCENDING -> {
               simpleQuery.append("ORDER BY name DESC")
           }
           SortType.RANDOM -> {
               simpleQuery.append("ORDER BY RANDOM()")
           }
       }
       return SimpleSQLiteQuery(simpleQuery.toString())
   }
}
```

[3] Modify the getAllStudents method to use SupportSQLiteQuery.
```
    @RawQuery(observedEntities = [Student::class])
    fun getAllStudent(query: SupportSQLiteQuery): LiveData<List<Student>>
```
[4] If you add parameters to the method, you also need to add parameters to the StudentRepository class.
```
    fun getAllStudent(sortType: SortType): LiveData<List<Student>> {
        val query = SortUtils.getSortedQuery(sortType)
        return studentDao.getAllStudent(query)
    }
```

[5] Add the lifecycle-livedata-ktx library to build.gradle(module: app) to use KTX on Transformation LiveData.

```
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
```

[6] Add another parameter to the MainViewModel for sorting.
```
    private val _sort = MutableLiveData<SortType>()
    init {
        _sort.value = SortType.ASCENDING
    }
    fun changeSortType(sortType: SortType) {
        _sort.value = sortType
    }

    fun getAllStudent(): LiveData<List<Student>> = _sort.switchMap {
        studentRepository.getAllStudent(it)
    }
```

[7] Create an option menu to provide sorting options
```
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/action_ascending"
        android:title="@string/ascending" />
    <item
        android:id="@+id/action_descending"
        android:title="@string/descending" />
    <item
        android:id="@+id/action_random"
        android:title="@string/random" />
</menu>
```

[8] Add resource strings in the strings.xml file
```
<string name="sort">Sort</string>
<string name="ascending">A-Z</string>
<string name="descending">Z-A</string>
<string name="random">Random</string>
```

[9] Before adding the new option menu item, first add an icon for it. To do this, right-click on the drawable folder in your project's res directory and select New > Vector Asset. Name the asset ic_sort and search for Clip Art with the name sort. Then, click Next and Finish.  

[10] Add a dedicated sorting item to the option_menu.xml file.
```
    <item
        android:id="@+id/action_sort"
        android:icon="@drawable/ic_sort"
        android:title="@string/sort"
        app:showAsAction="ifRoom" />
```

[11] Implement a Raw Query on the main page using existing data.
```
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_single_table -> {
                getStudent()
                showSortingOptionMenu(true)
                return true
            }
            R.id.action_many_to_one -> {
                getStudentAndUniversity()
                showSortingOptionMenu(false)
                true
            }
            R.id.action_one_to_many -> {
                getUniversityAndStudent()
                showSortingOptionMenu(false)
                true
            }

            R.id.action_many_to_many -> {
                getStudentWithCourse()
                showSortingOptionMenu(false)
                true
            }

            R.id.action_show_all_data -> {
                getAllStudentAndUniversityWithCourse()
                showSortingOptionMenu(false)
                true
            }

            R.id.action_sort -> {
                showSortingPopupMenu()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSortingPopupMenu() {
        val view = findViewById<View>(R.id.action_sort) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.sorting_menu, menu)
            setOnMenuItemClickListener {
                mainViewModel.changeSortType(
                    when (it.itemId) {
                        R.id.action_ascending -> SortType.ASCENDING
                        R.id.action_descending -> SortType.DESCENDING
                        else -> SortType.RANDOM
                    }
                )
                true
            }
            show()
        }
    }
    
    private fun showSortingOptionMenu(isShow: Boolean) {
        val view = findViewById<View>(R.id.action_sort) ?: return
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

```