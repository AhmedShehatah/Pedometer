package com.course.pedometer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

class TimerService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        getTime()
        Log.d("timerService", "get ticked")
        return START_NOT_STICKY

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun getTime() {
        val dateInHour = SimpleDateFormat("HH", Locale.ENGLISH)
        val dateInMin = SimpleDateFormat("mm", Locale.ENGLISH)
        val date = Date()
        val hour = dateInHour.format(date)
        val min = dateInMin.format(date)
        if (hour == "01" && (min == "00" || min == "01")) {
            PedometerSharedPreferences().setInt(MyApp.steps, 0)
            notification()
        }
    }

    private fun notification() {
        val numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notificationLayout = RemoteViews(packageName!!, R.layout.custom_notification)
        notificationLayout.setTextViewText(R.id.tvCalsNot, (numSteps / 19.3).toInt().toString())
        notificationLayout.setTextViewText(R.id.tvRunNot, numSteps.toString())
        val customNotification = NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setSilent(true)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, customNotification)
        PedometerSharedPreferences().setInt(MyApp.steps, 0)

        Log.d("FromTimer", "FromTimer")

    }
}