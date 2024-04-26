// TODO tambahkan terlebih dahulu library Room-Paging pada build.gradle (module: app)
```
freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
```

// TODO tambahkan library yang dibutuhkan build.gradle (module: app)
```
    implementation("androidx.room:room-paging:2.6.1")
    
```

// TODO Buatlah file baru pada package database dengan nama QuoteDao. Kemudian buatlah fungsi untuk memasukkan, membaca, dan menghapus data
```
@Dao
interface QuoteDao {
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertQuote(quote: List<QuoteResponseItem>)

    @Query("SELECT * FROM quote")
    fun getAllQuote(): PagingSource<Int, QuoteResponseItem>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()
}
```

TODO Tambahkan QuoteDao ke dalam QuoteDatabase
```    
   abstract fun quoteDao(): QuoteDao
```

// TODO Buatlah file baru pada package data dengan nama QuoteRemoteMediator untuk membuat logika untuk online offline dengan menggunakan RemoteMediator.
```
@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
   private val database: QuoteDatabase,
   private val apiService: ApiService
) : RemoteMediator<Int, QuoteResponseItem>() {
 
   private companion object {
       const val INITIAL_PAGE_INDEX = 1
   }
   override suspend fun initialize(): InitializeAction {
       return InitializeAction.LAUNCH_INITIAL_REFRESH
   }
 
   override suspend fun load(
       loadType: LoadType,
       state: PagingState<Int, QuoteResponseItem>
   ): MediatorResult {
       val page = INITIAL_PAGE_INDEX
       try {
           val responseData = apiService.getQuote(page, state.config.pageSize)
           val endOfPaginationReached = responseData.isEmpty()
           database.withTransaction {
               if (loadType == LoadType.REFRESH) {
                   database.quoteDao().deleteAll()
               }
               database.quoteDao().insertQuote(responseData)
           }
           return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
       } catch (exception: Exception) {
           return MediatorResult.Error(exception)
       }
   }
}
```

// TODO Tambahkan properti remoteMediator dan ganti pagingSourceFactory dengan data dari database
```
class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService) {
   fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
       @OptIn(ExperimentalPagingApi::class)
       return Pager(
           config = PagingConfig(
               pageSize = 5
           ),
           remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService),
           pagingSourceFactory = {
//                QuotePagingSource(apiService)
               quoteDatabase.quoteDao().getAllQuote()
           }
       ).liveData
   }
}
```

## Remote Keys
// TODO Buatlah file baru baru bernama RemoteKeys pada package database
```
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
```

// TODO Buatlah file baru pada package database dengan nama RemoteKeysDao dan buatlah fungsi untuk memasukkan, membaca, dan menghapus data
```
@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}
```

// TODO Memperbarui kode untuk Remote Keys di QuoteRemoteMediator
```
@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
   private val database: QuoteDatabase,
   private val apiService: ApiService
) : RemoteMediator<Int, QuoteResponseItem>() {
 
   private companion object {
       const val INITIAL_PAGE_INDEX = 1
   }
 
   override suspend fun initialize(): InitializeAction {
       return InitializeAction.LAUNCH_INITIAL_REFRESH
   }
 
   override suspend fun load(
       loadType: LoadType,
       state: PagingState<Int, QuoteResponseItem>
   ): MediatorResult {
       val page = when (loadType) {
           LoadType.REFRESH ->{
               val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
               remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
           }
           LoadType.PREPEND -> {
               val remoteKeys = getRemoteKeyForFirstItem(state)
               val prevKey = remoteKeys?.prevKey
                   ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
               prevKey
           }
           LoadType.APPEND -> {
               val remoteKeys = getRemoteKeyForLastItem(state)
               val nextKey = remoteKeys?.nextKey
                   ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
               nextKey
           }
       }
 
       try {
           val responseData = apiService.getQuote(page, state.config.pageSize)
 
           val endOfPaginationReached = responseData.isEmpty()
 
           database.withTransaction {
               if (loadType == LoadType.REFRESH) {
                   database.remoteKeysDao().deleteRemoteKeys()
                   database.quoteDao().deleteAll()
               }
               val prevKey = if (page == 1) null else page - 1
               val nextKey = if (endOfPaginationReached) null else page + 1
               val keys = responseData.map {
                   RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
               }
               database.remoteKeysDao().insertAll(keys)
               database.quoteDao().insertQuote(responseData)
           }
           return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
       } catch (exception: Exception) {
           return MediatorResult.Error(exception)
       }
   }
 
   private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
       return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
           database.remoteKeysDao().getRemoteKeysId(data.id)
       }
   }
 
   private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
       return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
           database.remoteKeysDao().getRemoteKeysId(data.id)
       }
   }
 
   private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
       return state.anchorPosition?.let { position ->
           state.closestItemToPosition(position)?.id?.let { id ->
               database.remoteKeysDao().getRemoteKeysId(id)
           }
       }
   }
 
}
}
```

##Testing
// TODO tambahkan library yang dibutuhkan build.gradle (module: app)
```
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0") //InstantTaskExecutorRule
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") //TestDispatcher
    testImplementation("androidx.arch.core:core-testing:2.2.0") // InstantTaskExecutorRule
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") //TestDispatcher
```

// TODO buat dua kelas baru pada folder com.dicoding.myunlimitedquotes(test) bernama LiveDataTestUtil seperti yang sudah kita pelajari pada materi Advanced Testing.
```
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
 
    try {
        afterObserve.invoke()
 
        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
 
    } finally {
        this.removeObserver(observer)
    }
 
    @Suppress("UNCHECKED_CAST")
    return data as T
}

suspend fun <T> LiveData<T>.observeForTesting(block: suspend  () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}
```

// TODO buat dua kelas baru pada folder com.dicoding.myunlimitedquotes(test) bernama MainDispatcherRule seperti yang sudah kita pelajari pada materi Advanced Testing.
```
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
override fun starting(description: Description) {
Dispatchers.setMain(testDispatcher)
}

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

// TODO buat berkas baru sekali kali dengan nama DataDummy
```
object DataDummy {

    fun generateDummyQuoteResponse(): List<QuoteResponseItem> {
        val items: MutableList<QuoteResponseItem> = arrayListOf()
        for (i in 0..100) {
            val quote = QuoteResponseItem(
                i.toString(),
                "author + $i",
                "quote $i",
            )
            items.add(quote)
        }
        return items
    }
}
```

// TODO Tuliskan kode untuk pengujian setiap case dalam MainViewTest
```
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
 
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
 
    @Mock
    private lateinit var quoteRepository: QuoteRepository
 
    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<QuoteResponseItem> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<QuoteResponseItem>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)
        
        val mainViewModel = MainViewModel(quoteRepository)
        val actualQuote: PagingData<QuoteResponseItem> = mainViewModel.quote.getOrAwaitValue()
 
        val differ = AsyncPagingDataDiffer(
            diffCallback = QuoteListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
 
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }
 
    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<QuoteResponseItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<QuoteResponseItem>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)
        val mainViewModel = MainViewModel(quoteRepository)
        val actualQuote: PagingData<QuoteResponseItem> = mainViewModel.quote.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = QuoteListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}
 
class QuotePagingSource : PagingSource<Int, LiveData<List<QuoteResponseItem>>>() {
    companion object {
        fun snapshot(items: List<QuoteResponseItem>): PagingData<QuoteResponseItem> {
            return PagingData.from(items)
        }
    }
 
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<QuoteResponseItem>>>): Int {
        return 0
    }
 
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<QuoteResponseItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
 
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

```

// TODO Siapakan dulu fake untuk api dan database pada QuoteRemoteMediatorTest
```
class FakeApiService : ApiService {

    override suspend fun getQuote(page: Int, size: Int): List<QuoteResponseItem> {
        val items: MutableList<QuoteResponseItem> = arrayListOf()

        for (i in 0..100) {
            val quote = QuoteResponseItem(
                i.toString(),
                "author + $i",
                "quote $i",
            )
            items.add(quote)
        }

        return items.subList((page - 1) * size, (page - 1) * size + size)
    }
}
```

// TODO Kemudian buat testing sesuai dengan skenario yang sudah dibuat. 
```
@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class Test {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: QuoteDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        QuoteDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = QuoteRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, QuoteResponseItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}
```