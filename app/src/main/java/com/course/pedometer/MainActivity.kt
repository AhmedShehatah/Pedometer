package com.course.pedometer

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.course.pedometer.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), SensorEventListener, StepListener {
    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null

    private var numSteps: Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.llRoot.background.alpha = 80

        // Get an instance of the SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)

        numSteps = PedometerSharedPreferences().getInt("steps", 0)
        binding.tvSteps.text = numSteps.toString()
        binding.tvCals.text = (numSteps / 15.3).toInt().toString()


        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_FASTEST
        )

        myWorkManager()

        startService()
        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, CourseActivity::class.java))
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
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

    override fun step(timeNs: Long) {
        numSteps++
        binding.tvSteps.text = numSteps.toString()
        binding.tvCals.text = (numSteps / 15.3).toInt().toString()
        PedometerSharedPreferences().setInt("steps", numSteps)
    }

    private fun myWorkManager() {
        val constraints = Constraints.Builder().setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()
        val myRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            24,
            TimeUnit.HOURS

        ).setConstraints(constraints).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, myRequest)


    }

    private fun startService() {
        val intent = Intent(this, Service::class.java)
        intent.putExtra("steps", numSteps)
        startService(intent)
    }

}
