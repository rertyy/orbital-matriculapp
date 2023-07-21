package com.example.frontend.ui.screens.home

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
        var listEvents: List<Event>? = null
        viewModelScope.launch {
            listEvents = try {
                eventRepository.get().getEvents().body()
                    ?: throw Exception("Events is null")
            } catch (e: Exception) {
                null
            }
        }

        return listEvents
    }


}

// TODO add @SerializedName to use json format
data class Event(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("name") val eventName: String,
    @SerializedName("body") val body: String,
//    val eventStartDate: String?,
//    val eventEndDate: String?,
)
