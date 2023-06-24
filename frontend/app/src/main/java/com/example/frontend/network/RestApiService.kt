package com.example.frontend.network


import com.example.frontend.ui.screens.LoginRequest
import com.example.frontend.ui.screens.LoginResponse
import com.example.frontend.ui.screens.Post
import com.example.frontend.ui.screens.RegisterRequest
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET


private const val BASE_URL =
    "http://10.0.2.2:8080/" // TODO backend URL to host.
// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused

interface ApiService {
    @POST("/login")
    suspend fun authenticateLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterRequest>

    @GET("/posts")
    suspend fun getAllPosts(): List<Post>

}

// TODO: comment the below out and replace w DI
// to keep as comment for posterity
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object RestApiService {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}