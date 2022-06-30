package com.course.pedometer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class Service : Service(), SensorEventListener, StepListener {
    private lateinit var notification: Notification
    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var numSteps: Int = 0
    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)

        numSteps = PedometerSharedPreferences().getInt("steps", 0)

        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_FASTEST
        )

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)


        notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Number of steps")
            .setContentText(numSteps.toString())
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccelerometer(
                event.timestamp,
                event.values[0],
                event.values[1],
                event.values[2]
            )
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun step(timeNs: Long) {
        numSteps++
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)


        notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Number of steps")
            .setContentText(numSteps.toString())
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()
        startForeground(1, notification)
        PedometerSharedPreferences().setInt("steps", numSteps)
        Log.d("stepsss", PedometerSharedPreferences().getInt("steps", 0).toString())

    }
}