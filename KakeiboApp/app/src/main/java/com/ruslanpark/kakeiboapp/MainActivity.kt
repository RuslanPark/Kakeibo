package com.ruslanpark.kakeiboapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ruslanpark.kakeiboapp.databinding.ActivityMainBinding
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {

        binding.calendarView.setOnDateChangeListener { _, i, i2, i3 ->
            val str = "$i3.$i2.$i"
            binding.textView.text = str
        }

        super.onResume()
    }
    fun showDate(view : View) {
        val selectedDate = binding.calendarView.date
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate
        val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)

        val str = "Current date: ${dateFormatter.format(calendar.time)}"
        binding.textView.text = str
    }
}