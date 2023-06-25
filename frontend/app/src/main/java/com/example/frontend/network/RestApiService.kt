package com.example.frontend.network


import com.example.frontend.ui.screens.Event
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
import retrofit2.http.PUT
import retrofit2.http.Path


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

    // TODO this might be a Flow<> instead of a List
    @GET("/posts")
    suspend fun getAllPosts(): Response<List<Post>>

    @GET("/events")
    suspend fun getAllEvents(): Response<List<Event>>

    @PUT("/{categoryId}/{postId}/edit")
    suspend fun editPost(
        @Path("categoryId") categoryId: Int,
        @Path("postId") postId: Int,
        @Body request: Post
    ): Response<Post>

    @POST("/{categoryId}/addPost")
    suspend fun addPost(
        @Path("categoryId") categoryId: Int,
        @Body request: Post
    ): Response<Post>


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