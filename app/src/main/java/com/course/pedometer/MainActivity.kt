package com.course.pedometer

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.course.pedometer.databinding.ActivityMainBinding

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

        numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)
        binding.tvSteps.text = numSteps.toString()
        binding.tvCals.text = (numSteps / 19.3).toInt().toString()
        sensorManager!!.registerListener(
            this,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_FASTEST
        )
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
        numSteps = PedometerSharedPreferences().getInt(MyApp.steps, 0)

        binding.tvSteps.text = numSteps.toString()
        binding.tvCals.text = (numSteps / 19.3).toInt().toString()
        PedometerSharedPreferences().setInt(MyApp.steps, numSteps)
    }


    private fun startService() {
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }


}
