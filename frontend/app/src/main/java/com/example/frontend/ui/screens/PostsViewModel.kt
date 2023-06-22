package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

data class Post(
    val title: String,
    val body: String,
    val categoryName: String,
    val createdBy: String,
    val createdAt: String,
    val lastUpdated: String
)


sealed interface PostsUiState {
    data class Success(val posts: List<Post>) : PostsUiState
    object Error : PostsUiState
    object Loading : PostsUiState
}


class PostsViewModel: ViewModel() {
    var postsUiState: PostsUiState by mutableStateOf(PostsUiState.Loading)
        private set

    init {
        getAllPosts()
    }


    fun getAllPosts() {
        Log.d("GET posts", "Retrieving posts")

        viewModelScope.launch {
            postsUiState = PostsUiState.Loading
            postsUiState = try {
                val listPosts = RestApiService.retrofitService.getAllPosts()
                PostsUiState.Success(listPosts)
            } catch (e: IOException) {
                PostsUiState.Error
            } catch (e: HttpException) {
                PostsUiState.Error
            }
        }
    }

}