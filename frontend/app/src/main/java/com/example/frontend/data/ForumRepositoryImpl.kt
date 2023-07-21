package com.example.frontend.data

import Reply
import Thread
import com.example.frontend.network.ApiService
import com.example.frontend.repository.ForumRepository
import retrofit2.Response
import javax.inject.Inject


class ForumRepositoryImpl @Inject constructor(private val api: ApiService) : ForumRepository {
    override suspend fun getAllThreads(): Response<List<Thread>> {
        return api.getAllThreads()
    }

    override suspend fun addThread(id: Int, request: Thread): Response<Thread> {
        return api.addThread(id, request)
    }

    override suspend fun getThread(id: Int): Response<Thread> {
        return api.getThread(id)
    }

    override suspend fun getThreadReplies(id: Int): Response<List<Reply>> {
        return api.getThreadReplies(id)
    }

    override suspend fun editThread(id: Int, request: Thread): Response<Thread> {
        return api.editThread(id, request)
    }

    override suspend fun deleteThread(id: Int): Response<Int> {
        return api.deleteThread(id)
    }

    override suspend fun newReply(id: Int, request: Reply): Response<Reply> {
        return api.newReply(id, request)
    }
}