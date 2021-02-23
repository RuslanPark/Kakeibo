package com.ruslanpark.kakeiboapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ruslanpark.kakeiboapp.R
import com.ruslanpark.kakeiboapp.databinding.FragmentCalendarBinding
import com.ruslanpark.kakeiboapp.model.Table
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var tableViewModel: TableViewModel

    private val PREF = "settings"
    private val PREF_MONTH = "month"
    private val PREF_YEAR = "year"
    private lateinit var appSettings: SharedPreferences
    var currentYear = 0
    var currentMonth = 0

    private val monthArray = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        currentYear = appSettings.getInt(PREF_YEAR, 0)
        currentMonth = appSettings.getInt(PREF_MONTH, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewModel = activity?.run {
            ViewModelProvider(this).get(TableViewModel::class.java)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        if (currentMonth != 0 && currentYear != 0) {
            binding.calendarView.currentDate = CalendarDay.from(currentYear, currentMonth, 1)
        }
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            goToPurchaseList(widget, date)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun goToPurchaseList(widget : MaterialCalendarView, date : CalendarDay) {
        if (date.month == currentMonth && date.year == currentYear) {
            val action = CalendarFragmentDirections.actionCalendarFragmentToDayFinancesFragment(date.day)
            widget.findNavController().navigate(action)
        } else {
            activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle("New month!")
                    .setMessage("Start a new month!\nIf you have already started a month, it's data will be lost")
                    .setIcon(R.drawable.ic_new)
                    .setPositiveButton("Yes") {
                            _, _ ->
                        run {
                            createNewData()
                            //dialog.cancel()
                        }
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }
        }
    }

    private fun createNewData() {
        currentMonth = binding.calendarView.selectedDate?.month ?: 0
        currentYear = binding.calendarView.selectedDate?.year ?: 0
        appSettings.edit().putInt(PREF_MONTH, currentMonth).putInt(PREF_YEAR, currentYear).apply()

        val result = mutableListOf<Table>()
        val table = Table(0, arrayListOf())

        val calendar = Calendar.getInstance()
        calendar.set(currentYear, currentMonth - 1, 1)
        val num = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..num) {
            result.add(i - 1, table)
        }

        tableViewModel.clearData()
        tableViewModel.insertAllData(result)
    }
}