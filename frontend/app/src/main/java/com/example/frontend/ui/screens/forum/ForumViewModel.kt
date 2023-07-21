package com.example.frontend.ui.screens.forum

import Reply
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import Thread
import android.content.SharedPreferences
import com.example.frontend.data.TokenStore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext


sealed interface ForumUiState {
    data class Success(val threadList: List<Thread>) : ForumUiState

    data class Success2(
        val thread: Thread,
        val replies: List<Reply>
    ) : ForumUiState

    object Error : ForumUiState
    object Loading : ForumUiState

}

@HiltViewModel
class ForumViewModel @Inject constructor(
) : ViewModel() {

    var forumUiState: ForumUiState by mutableStateOf(ForumUiState.Loading)
        private set

    init {
        getAllThreads()
    }


    fun addThread(thread: Thread) {
        Log.d("FORUM", "adding post")
        viewModelScope.launch {
            try {
                RestApiService.retrofitService.addThread(thread.threadId, thread)
            } catch (e: Exception) {
                // TODO change pokemon
                Log.d("FORUM", "Error: ${e.message}")
            }
        }
        getAllThreads()
    }

    fun modifyThread(oldThread: Thread, newThread: Thread) {
        Log.d("FORUM", "modifying post")
        viewModelScope.launch {
            try {
                RestApiService.retrofitService.editThread(
                    oldThread.threadId,
                    newThread
                )
            } catch (e: Exception) {
                // TODO change pokemon
                Log.d("FORUM", "Error editing post: ${e.message}")
            }
        }
        getAllThreads()
    }

    fun getAllThreads() {
        Log.d("FORUM", "Retrieving posts")

        viewModelScope.launch {
            try {
            } catch (e: Exception) {
                Log.d("FORUM", "Error reading token: ${e.message}")
            }
        }

        // TODO change exception handling
        viewModelScope.launch {
            forumUiState = ForumUiState.Loading
            forumUiState = try {
                val response = RestApiService.retrofitService.getAllThreads()
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

    //TODO: correct this
    fun deleteThread(threadId: Int) {
        Log.d("FORUM", "Delete posts")

        viewModelScope.launch {
            try {
                RestApiService.retrofitService.deleteThread(
                    threadId
                )
            } catch (e: Exception) {
                Log.d("FORUM", "Error deleting post: ${e.message}")
            }
        }

        getAllThreads()
    }

    //TODO: correct this
    fun getReplies(threadId: Int) {
        Log.d("FORUM", "Get thread")


        viewModelScope.launch {
            forumUiState = ForumUiState.Loading
            forumUiState = try {
                val response1 = RestApiService.retrofitService.getThread(threadId)
                val response2 = RestApiService.retrofitService.getThreadReplies(threadId)

                val thread = response1.body()
                val replies = response2.body()

                Log.d("FORUM", "success getAllReplies $thread $replies")

                if (thread == null || replies == null) {
                    ForumUiState.Error
                } else {
                    ForumUiState.Success2(thread, replies)
                }
            } catch (e: Exception) {
                Log.d("FORUM", "Error getting thread: ${e.message}")
                ForumUiState.Error
            }
        }

    }

    fun addReply(reply: Reply) {
        Log.d("FORUM", "Add Reply")

        viewModelScope.launch {
            try {
                RestApiService.retrofitService.newReply(reply.threadId, reply)
            } catch (e: Exception) {
                Log.d("FORUM", "Error getting thread: ${e.message}")
            }
        }

        getReplies(reply.threadId)
    }
}