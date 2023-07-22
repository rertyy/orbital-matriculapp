package com.example.frontend.ui.screens

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.frontend.EventReceiver
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

        alarmManager.setExactAndAllowWhileIdle(   //permission needed from users
            AlarmManager.RTC_WAKEUP,
            event.eventStartDate.toLocalDateTime().atZone(ZoneId.systemDefault())
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