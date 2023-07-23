package com.example.frontend.ui.screens.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.frontend.EventReceiver
import parseStringToDateTime
import java.time.ZoneId

interface EventSchedulerInterface {
    fun schedule(event: Event)
    fun cancel(event: Event)
}


class EventScheduler(
    private val context: Context
) : EventSchedulerInterface {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(event: Event) {
        val intent = Intent(context, EventReceiver::class.java).apply {
            putExtra("MatriculApp scheduled event: ", event.eventName)
        }
        val dateTimeParsed = parseStringToDateTime(event.eventStartDate)
        Log.d("EventScheduler", "schedule: $dateTimeParsed")

        alarmManager.setExactAndAllowWhileIdle(   //permission needed from users
            AlarmManager.RTC_WAKEUP,
            dateTimeParsed.toLocalDateTime().atZone(ZoneId.systemDefault())
                .toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                event.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        )
    }

    override fun cancel(event: Event) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                event.hashCode(),
                Intent(context, EventReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}