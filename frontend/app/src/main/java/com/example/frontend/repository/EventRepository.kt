package com.example.frontend.repository

import com.example.frontend.ui.screens.home.Event
import retrofit2.Response

interface EventRepository {
    suspend fun getEvents(): Response<List<Event>>
}