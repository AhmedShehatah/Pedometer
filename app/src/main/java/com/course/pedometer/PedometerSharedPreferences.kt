package com.course.pedometer

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PedometerSharedPreferences {

    private var pedometerSharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    @SuppressLint("CommitPrefEdits")
    private fun openConnection() {
        pedometerSharedPreferences = MyApp.appContext.getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
        editor = pedometerSharedPreferences!!.edit()
    }

    private fun closeConnection() {
        pedometerSharedPreferences = null
        editor = null
    }


    fun setInt(key: String, defValue: Int) {
        openConnection()
        editor!!.putInt(key, defValue)
        editor!!.commit()
        closeConnection()
    }


    fun getInt(key: String, defValue: Int): Int {
        var result = defValue
        openConnection()
        if (pedometerSharedPreferences!!.contains(key)) {
            result = pedometerSharedPreferences!!.getInt(key, defValue)
        }
        closeConnection()
        return result
    }


    companion object {
        const val APP_PREFERENCES = "Pedometer"
    }
}