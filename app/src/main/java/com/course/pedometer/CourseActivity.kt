package com.course.pedometer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.course.pedometer.databinding.ActivityCourseBinding


class CourseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val (firstSen, secondSen) = listOf("جوري", "كبسولة شقية لسد الشهية")
        val next = "<font color='#3F51B5'>GORI</font>"
        val (thSen, forthSen) = listOf("ريجفي", "سبولة مضافة لحرق الدهون ")
        val second = "<font color='#F44336'>REGI-V</font>"
        binding.tvDrugsNames.text =
            Html.fromHtml("$firstSen $next $secondSen<br/>$thSen $second $forthSen")
        val word = "<font color='#3F51B5'><u>اتصل بنا</u></font>"
        binding.tvCallUs.text = Html.fromHtml("لمعلومات اكثر...$word")
        binding.tvCallUs.setOnClickListener {
            open()
        }
        binding.btnGet.setOnClickListener {
            open()
        }


    }

    private fun appInstalledOrNot(url: String): Boolean {
        val packageManager = packageManager
        val appInstalled: Boolean = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }

    private fun open() {
        Log.d("here", "Clicked")
        val isWhatsAppInstalled = appInstalledOrNot("com.whatsapp")
        if (isWhatsAppInstalled) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+201014777264")
            startActivity(intent)
            Log.d("here", "Installed")
        } else {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+201014777264")
            startActivity(intent)
        }
    }
}