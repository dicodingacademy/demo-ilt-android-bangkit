You can follow this instruction to implement Android Architecture Component 

[0] ViewModel and LiveData
```
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") //viewModelScope
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0") //liveData
```

[1] Create Repository to Manage Network Data Source.
```
class UserRepository private constructor(
private val apiService: ApiService
) {

    private val _users = MutableLiveData<List<DataItem>>()
    val users: LiveData<List<DataItem>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError
    
}
```

[2] Move getUser from Activity to Repository and give some improvement
```
    fun getUser() {
        _isLoading.value = true
        val client = apiService.getListUsers("1")

        client.enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _users.value = response.body()?.data as List<DataItem>
                    } else {
                        _isError.value = "onFailure: ${response.message()}"
                    }
                } else {
                    _isError.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                _isLoading.value = false
                _isError.value = "onFailure: ${t.message.toString()}"
                t.printStackTrace()
            }
        })
    }
```

[3] Create Singleton Initialization for this Class Repository
```
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
```

[4] Create Simple Injection to provide Repository
```
object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}
```

[5] Create ViewModel
```
class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    val users = userRepository.users
    val isLoading = userRepository.isLoading
    val isError = userRepository.isError

    fun getUser() { userRepository.getUser() }
}
    
```

[6] Create ViewModel Factory
```
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return MainViewModel(Injection.provideRepository()) as T
            }
        }
    }
```

[7] Instance ViewModel using MainViewModel Factory.
```val mainViewModel = ViewModelProvider(this, MainViewModel.Factory).get(MainViewModel::class.java)```


[8] Don't forget to add a ProgressBar to your layout
```
val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

recyclerView.setHasFixedSize(true)
recyclerView.layoutManager = LinearLayoutManager(this)
recyclerView.adapter = adapter
```

   
[9] Call Get User from ViewModel
```mainViewModel.getUser()```

[10] Observe data
```        
        mainViewModel.isLoading.observe(this) { loadingState ->
            if (loadingState) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        mainViewModel.users.observe(this) { users ->
            for (newUser in users) {
                adapter.addUser(newUser)
            }
        }

        mainViewModel.isError.observe(this) { errorMessage ->
            Toast.makeText(
                this,
                "Terjadi kesalahan (${errorMessage})",
                Toast.LENGTH_SHORT
            ).show()
        }
```
