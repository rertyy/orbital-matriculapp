package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// TODO add postId and categoryId to Post
data class Post(
    val title: String,
    val body: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("created_by") val createdBy: Int,
    @SerializedName("created_by_name") val createdByName: String,
//    @SerializedName("created_at") val createdAt: OffsetDateTime,
//    @SerializedName("last_updated") val lastUpdated: OffsetDateTime
)


sealed interface PostsUiState {
    data class Success(val posts: List<Post>) : PostsUiState
    object Error : PostsUiState
    object Loading : PostsUiState
}


class PostsViewModel : ViewModel() {
    var postsUiState: PostsUiState by mutableStateOf(PostsUiState.Loading)
        private set

    init {
        getAllPosts()
    }

    fun addPost(post: Post) {
        Log.d("FORUM", "adding post")
        viewModelScope.launch {
            try {
                // TODO dont hardcode the id
                RestApiService.retrofitService.addPost("1", post)
            } catch (e: Exception) {
                // TODO change pokemon
                Log.d("FORUM", "Error: ${e.message}")
            }
        }
        getAllPosts()
    }


    fun getAllPosts() {
        Log.d("FORUM", "Retrieving posts")

        // TODO change exception handling
        viewModelScope.launch {
            postsUiState = PostsUiState.Loading
            postsUiState = try {
                Log.d("FORUM", "Retrieving posts2")
                val response = RestApiService.retrofitService.getAllPosts()
                Log.d("FORUM", "Retrieving posts3")
                val listPosts = response.body()
                Log.d("FORUM", "success $listPosts")
                if (listPosts == null) {
                    PostsUiState.Error
                } else PostsUiState.Success(listPosts)
            } catch (e: IOException) {
                Log.d("FORUM", "Error: ${e.message}")
                PostsUiState.Error
            } catch (e: HttpException) {
                Log.d("FORUM", "Error: ${e.message}")
                PostsUiState.Error
            }
        }
    }

}