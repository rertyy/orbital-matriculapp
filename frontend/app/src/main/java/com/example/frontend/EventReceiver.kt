package com.example.frontend

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class EventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MatriculApp scheduled event: ") ?: return

        println("Matriculapp event scheduled: $message")
    }
}