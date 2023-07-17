package com.example.frontend.network


import com.example.frontend.ui.screens.Event
import com.example.frontend.ui.screens.LoginRequest
import com.example.frontend.ui.screens.LoginResponse
import com.example.frontend.ui.screens.Thread
import com.example.frontend.ui.screens.RegisterRequest
import com.example.frontend.ui.screens.Reply
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


//private const val BASE_URL =
//    "http://10.0.2.2:8080/"
//// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused

private const val BASE_URL =
    "https://orbital-backend-6z61.onrender.com"


interface ApiService {
    @POST("/login")
    suspend fun authenticateLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterRequest>

    // TODO this might be a Flow<> instead of a List
    @GET("/threads")
    suspend fun getAllThreads(): Response<List<Thread>>

    @GET("/events")
    suspend fun getAllEvents(): Response<List<Event>>

    @PUT("/{categoryId}/edit")
    suspend fun editThread(
        @Path("threadId") threadId: Int,
        @Body request: Thread
    ): Response<Thread>

    @POST("/{categoryId}/addThread")
    suspend fun addThread(
        @Path("categoryId") categoryId: Int,
        @Body request: Thread
    ): Response<Thread>

    @DELETE("/{threadId}/deleteThread")
    suspend fun deleteThread(
        @Path("threadId") threadId: Int,
    ): Response<Int>

    @GET("/{threadId}/getThread")
    suspend fun getThread(
        @Path("threadId") threadId: Int
    ): Response<Thread>

    @GET("/threads/{threadId}/replies")
    suspend fun getThreadReplies(
        @Path("threadId") threadId: Int
    ): Response<List<Reply>>

    @POST("/threads/{threadId}/replies/newreply")
    suspend fun newReply(
        @Path("threadId") threadId: Int,
        @Body request: Reply
    ): Response<Reply>
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