package com.example.frontend.ui.screens.home

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("event_name") val eventName: String,
    @SerializedName("event_body") val eventBody: String,
    @SerializedName("event_start_date") val eventStartDate: String,
    @SerializedName("event_end_date") val eventEndDate: String,
)
