package com.example.frontend.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


sealed interface EventsUiState {
    object Error : EventsUiState

    object Success : EventsUiState

    object Loading : EventsUiState
}


data class Event(
    @SerializedName("event_id") val eventId: Int = -1,
    @SerializedName("name") val eventName: String = "",
    @SerializedName("body") val eventBody: String = "",
    val eventStartDate: OffsetDateTime = OffsetDateTime.MIN,
    val eventEndDate: OffsetDateTime = OffsetDateTime.MIN
)

val defaultEvent = Event()

class EventsViewModel : ViewModel() {

    var eventsUiState: EventsUiState by mutableStateOf(EventsUiState.Loading)
        private set

    init {
        getAllEvents()
    }


    var events: List<Event> by mutableStateOf(listOf(defaultEvent))
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


    fun getAllEvents() {


        viewModelScope.launch {

            eventsUiState = EventsUiState.Loading


            eventsUiState = try {
                val eventsList = RestApiService.retrofitService.getEvents().body()

                if (eventsList == null) {
                    EventsUiState.Error
                } else {
                    events = eventsList
                    EventsUiState.Success
                }
            } catch (e: Exception) {
                Log.d("Events", "Error getting events: ${e.message}")
                EventsUiState.Error
            }
        }
    }

    fun setReminder(event: Event, context: Context) {

        val scheduler = EventScheduler(context)

        scheduler.schedule(event)
    }


}

// TODO add @SerializedName to use json format

