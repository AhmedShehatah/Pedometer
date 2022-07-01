package com.course.pedometer

import android.app.*
import android.content.Context
import android.content.Intent
import java.util.*

class MyApp : Application() {

    private val channelId = "channelId"
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        createNotificationChannel()
        startTimer()
    }

    private fun createNotificationChannel() {

        val serviceChannel = NotificationChannel(
            channelId,
            "Number of steps: ",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager =
            getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)

    }

    private fun startTimer() {
        val intent = Intent(this, TimerService::class.java)
        val pendingIntent = PendingIntent.getService(this, 100, intent, 0)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis,
            60 * 1000,
            pendingIntent
        )
    }

    companion object {
        lateinit var appContext: Context
        const val steps = "steps"
    }
}