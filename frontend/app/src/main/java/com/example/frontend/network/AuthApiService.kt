package com.example.frontend.network


import com.example.frontend.ui.screens.LoginRequest
import com.example.frontend.ui.screens.LoginResponse
import com.example.frontend.ui.screens.LoginViewModel
import com.example.frontend.ui.screens.RegisterRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body


private const val BASE_URL =
    "http://10.0.2.2:8080/" // TODO backend URL to host.
// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused

interface LoginApiService {
    @POST("/login")
    suspend fun authenticateLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterRequest>

}

// TODO: comment the below out and replace w DI
// to keep as comment for posterity
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object AuthApiService {
    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}