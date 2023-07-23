package com.example.frontend

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.frontend.ui.EventNotificationService

class EventReceiver(private val eventId: Int = 7) : BroadcastReceiver() {
    constructor() : this(7)

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MatriculApp scheduled event: ") ?: return

        val service = if (context == null) {
            return
        } else {
            EventNotificationService(context, eventId)
        }

        service.showNotification(message)
    }
}