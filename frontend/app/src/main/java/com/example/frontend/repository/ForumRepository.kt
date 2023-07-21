package com.example.frontend.repository

import Reply
import Thread
import retrofit2.Response

interface ForumRepository {
    suspend fun getAllThreads(): Response<List<Thread>>
    suspend fun addThread(id: Int, request: Thread): Response<Thread>
    suspend fun getThread(id: Int): Response<Thread>
    suspend fun getThreadReplies(id: Int): Response<List<Reply>>
    suspend fun editThread(id: Int, request: Thread): Response<Thread>
    suspend fun deleteThread(id: Int): Response<Int>
    suspend fun newReply(id: Int, request: Reply): Response<Reply>
}