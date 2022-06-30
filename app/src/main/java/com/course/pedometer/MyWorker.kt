package com.course.pedometer

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.d("simple worker", "we got here")
        PedometerSharedPreferences().setInt("steps", 0)
        return Result.success()
    }
}