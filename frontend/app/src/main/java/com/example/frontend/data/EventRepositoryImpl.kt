package com.example.frontend.data

import com.example.frontend.network.ApiService
import com.example.frontend.repository.EventRepository
import com.example.frontend.ui.screens.home.Event
import retrofit2.Response
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(private val api: ApiService) : EventRepository {
    override suspend fun getEvents(): Response<List<Event>> {
        return api.getEvents()
    }
}




