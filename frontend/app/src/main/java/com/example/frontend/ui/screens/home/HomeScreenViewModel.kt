package com.example.frontend.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.repository.EventRepository
import com.google.gson.annotations.SerializedName
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val eventRepository: Lazy<EventRepository>) :
    ViewModel() {
    init {
        eventRepository.get()
        getEvents()
    }

//    var currentTime by mutableStateOf("")
//        private set

//    fun getCurrentTime() {
//        viewModelScope.launch {
//            while (true) {
//                val currentTimeString = DateFormat.getDateTimeInstance()
//                    .format(System.currentTimeMillis())
//                currentTime = currentTimeString
//                delay(1000)
//                viewModelScope.launch(Dispatchers.){
//                    currentTime = currentTimeString
//                }
//            }
//        }
//    }


    fun getEvents(): List<Event>? {
        // TODO error screen
        Log.d("HomeScreenViewModel", "getEvents called")
        var listEvents: List<Event>? = null
        viewModelScope.launch {
            listEvents = try {
                eventRepository.get().getEvents().body()
                    ?: throw Exception("Events is null")
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "exception thrown $e")
                null
            }
        }

        return listEvents
    }


}

