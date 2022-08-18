package com.example.hooliganchallenge.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private val yyyy_MM_dd_HH_mm: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm",  Locale.ENGLISH)
    private val HHmm: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    private val MM_dd_HHmm: SimpleDateFormat = SimpleDateFormat("MM-dd HH:mm",  Locale.ENGLISH)

    @RequiresApi(Build.VERSION_CODES.O)
    fun date2DayTime(oldTime: Date): String? {
        val newTime = Date()
        try {
            val cal: Calendar = Calendar.getInstance()
            cal.time = newTime
            val oldCal: Calendar = Calendar.getInstance()
            oldCal.time = oldTime
            val oldYear: Int = oldCal.get(Calendar.YEAR)
            val year: Int = cal.get(Calendar.YEAR)
            val oldDay: Int = oldCal.get(Calendar.DAY_OF_YEAR)
            val day: Int = cal.get(Calendar.DAY_OF_YEAR)
            if (oldYear == year) {
                return when (oldDay - day) {
                    -1 -> {
                        "Yesterday, " + oldTime.let { HHmm.format(it) }
                    }
                    0 -> {
                        "Today, " + oldTime.let { HHmm.format(it) }
                    }
                    1 -> {
                        "Tomorrow, " + oldTime.let { HHmm.format(it) }
                    }
                    else -> {
                        MM_dd_HHmm.format(oldTime)
                    }
                }
            }

        } catch (e: Exception) {
        }
        return oldTime?.let { yyyy_MM_dd_HH_mm.format(it) }
    }
}
