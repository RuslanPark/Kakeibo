package com.ruslanpark.kakeiboapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ruslanpark.kakeiboapp.databinding.ActivityMainBinding
import java.text.DateFormat
import java.time.DayOfWeek
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val monthArray = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        binding.calendarView.topbarVisible = false
        binding.textView.text = monthArray[Calendar.MONTH - 1]
        binding.calendarView.setOnMonthChangedListener { widget, date ->
            binding.textView.text = monthArray[date.month - 1]
        }
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            
        }


        super.onResume()
    }
}