package com.example.frontend.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    var currentTime by mutableStateOf("")
        private set

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


    fun getAllEvents(): List<Event> {
        // TODO error screen
        var listEvents: List<Event> = listOf(Event("1", "Event 1", "", ""))
        viewModelScope.launch {
            try {
                val events = RestApiService.retrofitService.getAllEvents().body()
                if (events != null) {
                    listEvents = events
                } else {
                    throw Exception("Events is null")
                }
            } catch (e: Exception) {
                // TODO change pokemon

            }
        }

        return listEvents
    }


}

// TODO add @SerializedName to use json format
data class Event(
    val eventId: String,
    val eventName: String,
    val eventStartDate: String?,
    val eventEndDate: String?,
)
