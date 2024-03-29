package com.example.frontend.network


import com.example.frontend.ui.screens.auth.LoginRequest
import com.example.frontend.ui.screens.auth.LoginResponse
import com.example.frontend.ui.screens.auth.RegisterRequest
import com.example.frontend.ui.screens.forum.Reply
import com.example.frontend.ui.screens.forum.Thread
import com.example.frontend.ui.screens.home.Event
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//// NB https://stackoverflow.com/questions/5495534/java-net-connectexception-localhost-127-0-0-18080-connection-refused
private const val AWS_URL = "http://54.169.25.53"
private const val LOCALHOST = "http://10.0.2.2:8080/"
private const val RENDER = "https://orbital-backend-6z61.onrender.com"

private const val BASE_URL = AWS_URL


// private const
interface ApiService {
    @POST("/user/login")
    suspend fun authenticateLogin(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("/user/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterRequest>

    // TODO this might be a Flow<> instead of a List
    @GET("/threads/")
    suspend fun getAllThreads(): Response<List<Thread>>


    @POST("/threads/newThread")
    suspend fun addThread(
        @Body request: Thread
    ): Response<Thread>

    @GET("/threads/{threadId}")
    suspend fun getThread(
        @Path("threadId") threadId: Int
    ): Response<Thread>

    @GET("/threads/{threadId}/replies")
    suspend fun getThreadReplies(
        @Path("threadId") threadId: Int
    ): Response<List<Reply>>

    @PUT("/threads/{threadId}/edit")
    suspend fun editThread(
        @Path("threadId") threadId: Int,
        @Body request: Thread
    ): Response<Thread>

    @DELETE("/threads/{threadId}/delete")
    suspend fun deleteThread(
        @Path("threadId") threadId: Int,
    ): Response<Int>

    @POST("/threads/{threadId}/addReply")
    suspend fun newReply(
        @Path("threadId") threadId: Int,
        @Body request: Reply
    ): Response<Reply>

    @GET("/events/all")
    suspend fun getEvents(): Response<List<Event>>
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