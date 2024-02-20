You can follow this instruction to implement Coroutine

[0] Add Dependency for KTX
```    implementation("androidx.activity:activity-ktx:1.8.2") // Activity KTX```

[1] Call API using Coroutine
```
@GET("api/users")
suspend fun getListUsersUsingCoroutine(@Query("page") page: String): ResponseUser
```

[2] Change getUser using Coroutine
```
    fun getUser(): LiveData<Result<List<DataItem>>> = liveData {
    emit(Result.Loading)
        try {

            val response = apiService.getListUsersUsingCoroutine("1")
            if (response.data != null) {
                emit(Result.Success(response.data))
            } else {
                emit(Result.Error("Data Item is empty."))
            }

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
```

[3] Create SingleEvent for LiveData
```
sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

[4] Adjust your function
```    fun getUser() = userRepository.getUser()```


[5] Instance ViewModel using KTX 
```val mainViewModel: MainViewModel by viewModels { MainViewModel.Factory }```


[6] Adjust your code using Single Live Event
```
        mainViewModel.getUser().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        progressBar.visibility = View.GONE
                        for (newUser in result.data) {
                            adapter.addUser(newUser)
                        }
                    }

                    is Result.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan (${result.error})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
```