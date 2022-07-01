package com.course.pedometer

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat


class NotificationService : Service(), SensorEventListener, StepListener {

    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var numSteps: Int = 0
    override fun onCreate() {
        super.onCreate()



        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)

        numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)

        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_FASTEST
        )


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)
        notification()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
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
        numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)
        numSteps++
        notification()
    }

    private fun notification() {

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
        PedometerSharedPreferences().setInt(MyApp.steps, numSteps)

    }
}