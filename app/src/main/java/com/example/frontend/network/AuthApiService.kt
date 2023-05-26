package com.example.frontend.network


import com.example.frontend.ui.screens.LoginRequest
import com.example.frontend.ui.screens.LoginResponse
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
    "TODO: REPLACE WITH YOUR ACTUAL BACKEND URL" // TODO

interface LoginApiService {
    @POST("login")
    suspend fun authenticateLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL) // Replace with your actual backend URL
    // .client(okHttpClient) // seems like its unnecessary to make own?
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object AuthApiService {
    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}