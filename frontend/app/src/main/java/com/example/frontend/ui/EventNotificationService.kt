package com.example.frontend.ui

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.frontend.MainActivity
import com.example.frontend.R

class EventNotificationService(
    private val context: Context,
    val eventId: Int
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(message: String) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, EVENT_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_beenhere_24)
            .setContentTitle("MatriculApp scheduled event")
            .setContentText(message)
            .setContentIntent(activityPendingIntent)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }

    companion object {
        const val EVENT_CHANNEL_ID = "event_channel"
    }

}