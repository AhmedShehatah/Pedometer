package com.course.pedometer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApp : Application() {

    private val channelId = "channelId"
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val serviceChannel = NotificationChannel(
                channelId,
                "Number of steps: ",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

    }

    companion object {
        lateinit var appContext: Context
    }
}