package com.example.frontend.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import com.google.gson.annotations.SerializedName
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


    fun getAllEvents(): List<Event>? {
        // TODO error screen
        var listEvents: List<Event>? = null
        viewModelScope.launch {
            listEvents = try {
                RestApiService.retrofitService.getAllEvents().body()
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
