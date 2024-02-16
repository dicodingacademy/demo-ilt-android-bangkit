[0] Add Dependency Retrofit
```
implementation("com.google.code.gson:gson:2.10")
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
debugImplementation("com.github.chuckerteam.chucker:library:3.3.0")
```

[1] Add Response User
```
data class ResponseUser(
@field:SerializedName("page")
val page: Int? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem>? = null
)
```

[2] Add User Model
```
data class DataItem(
@field:SerializedName("id")
val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null
)
```

[3] Create an APIConfig to configure the interaction process with URLs.
```
class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
//                  .addInterceptor(ChuckerInterceptor(context))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
```

[4] Create API Service to Connect API by End Point
```
interface ApiService {
//get list users with query
@GET("api/users")
fun getListUsers(@Query("page") page: String): Call<ResponseUser>

    //get list user by id using path
    @GET("api/users/{id}")
    fun getUser(@Path("id") id: String): Call<ResponseUser>

    //post user using field x-www-form-urlencoded
    @FormUrlEncoded
    @POST("api/users")
    fun createUser(
        @Field("name") name: String,
        @Field("job") job: String
    ): Call<ResponseUser>

    //upload file using multipart
    @Multipart
    @PUT("api/uploadfile")
    fun updateUser(
        @Part("file") file: MultipartBody.Part,
        @PartMap data: Map<String, RequestBody>
    ): Call<ResponseUser>
}
```

[5] Call Client to get User Response && [6] Show data to RecyclerView
```
private fun getUser() {
    val client = ApiConfig.getApiService().getListUsers("1")
    client.enqueue(object : Callback<ResponseUser> {
        override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
            if (response.isSuccessful) {
                val dataArray = response.body()?.data as List<DataItem>
                for (data in dataArray) {
                    adapter.addUser(data)
                }
            }
        }

        override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
            Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            t.printStackTrace()
        }
    })
}    
```

[7] Add Permission to Access Internet.
```
<uses-permission android:name="android.permission.INTERNET"/>
```