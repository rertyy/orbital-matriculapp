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
    @SerializedName("post_id") val postId: Int = -1,
    val title: String,
    val body: String,
    @SerializedName("category_id") val categoryId: Int = -1,
    //@SerializedName("category_name") val categoryName: String,
    //@SerializedName("created_by") val createdBy: Int,
    //@SerializedName("created_by_name") val createdByName: String,

//    @SerializedName("created_at") val createdAt: OffsetDateTime,
//    @SerializedName("last_updated") val lastUpdated: OffsetDateTime
)


sealed interface ForumUiState {
    data class Success(val posts: List<Post>) : ForumUiState
    object Error : ForumUiState
    object Loading : ForumUiState

}


class ForumViewModel : ViewModel() {
    var forumUiState: ForumUiState by mutableStateOf(ForumUiState.Loading)
        private set

    init {
        getAllPosts()
    }

    fun addPost(post: Post) {
        Log.d("FORUM", "adding post")
        viewModelScope.launch {
            try {
                // TODO don't hardcode the id
                RestApiService.retrofitService.addPost(1, post)
            } catch (e: Exception) {
                // TODO change pokemon
                Log.d("FORUM", "Error: ${e.message}")
            }
        }
        getAllPosts()
    }

    fun modifyPost(oldPost: Post, newPost: Post) {
        Log.d("FORUM", "modifying post")
        viewModelScope.launch {
            try {
                RestApiService.retrofitService.editPost(
                    oldPost.categoryId,
                    oldPost.postId,
                    newPost
                )
            } catch (e: Exception) {
                // TODO change pokemon
                Log.d("FORUM", "Error editing post: ${e.message}")
            }
        }
        getAllPosts()
    }

    fun getAllPosts() {
        Log.d("FORUM", "Retrieving posts")

        // TODO change exception handling
        viewModelScope.launch {
            forumUiState = ForumUiState.Loading
            forumUiState = try {
                val response = RestApiService.retrofitService.getAllPosts()
                val listPosts = response.body()
                Log.d("FORUM", "success getAllPosts $listPosts")
                if (listPosts == null) {
                    ForumUiState.Error
                } else ForumUiState.Success(listPosts)
            } catch (e: IOException) {
                Log.d("FORUM", "Error getAllPosts: ${e.message}")
                ForumUiState.Error
            } catch (e: HttpException) {
                Log.d("FORUM", "Error getAllPosts: ${e.message}")
                ForumUiState.Error
            }
        }
    }

}