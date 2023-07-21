package com.example.frontend.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.frontend.repository.EventRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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
        Log.d("HomeScreenViewModel", "getEvents called")

        var listEvents: List<Event>? = null

        // this will block the UI thread but whatever. Proper way is to use
        // generally you put suspend functions inside the repository
        // and you call them inside your viewmodel, and setState the way the forum screen is done
        runBlocking {
            try {
                val deferredBody = async(Dispatchers.IO) {
                    eventRepository.get().getEvents().body()
                }
                val body = deferredBody.await()
                listEvents = body ?: throw Exception("Events is null")
                Log.d("HomeScreenViewModel", "events are $listEvents")
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "exception thrown $e")
            }
        }

        Log.d("HomeScreenViewModel", "returning $listEvents")
        return listEvents
    }


}

