package com.ruslanpark.kakeiboapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ruslanpark.kakeiboapp.R
import com.ruslanpark.kakeiboapp.adapter.RecycleViewAdapter
import com.ruslanpark.kakeiboapp.adapter.RecycleViewAdapterGlobal
import com.ruslanpark.kakeiboapp.databinding.FragmentCalendarBinding
import com.ruslanpark.kakeiboapp.model.Table
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel
import java.time.LocalDate
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var tableViewModel: TableViewModel

    private val PREF = "settings"
    private val PREF_MONTH = "month"
    private val PREF_YEAR = "year"
    private lateinit var appSettings: SharedPreferences
    private var currentYear = -1
    private var currentMonth = -1
    private lateinit var tableAdapter: RecycleViewAdapterGlobal

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        currentYear = appSettings.getInt(PREF_YEAR, -1)
        currentMonth = appSettings.getInt(PREF_MONTH, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewModel = activity?.run {
            ViewModelProvider(this).get(TableViewModel::class.java)
        }!!
        bindObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)


        if (currentMonth != -1 && currentYear != -1) {
            binding.calendarView.currentDate = CalendarDay.from(currentYear, currentMonth + 1, 1)
        }
        binding.calendarView.setOnDateChangedListener { widget, date, _ ->
            goToPurchaseList(widget, date)
        }
        showList()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun bindObservers() {
        tableViewModel.readAllData.observe(this, {
            val arrayOfPurchases = arrayListOf< Pair<String, Int> >()
            it.forEach { it1 -> arrayOfPurchases.addAll(it1.purchases)  }
            tableAdapter.update(arrayOfPurchases)
        })
    }

    private fun showList() {
        val arrayOfPurchases = arrayListOf< Pair<String, Int> >()
        tableViewModel.readAllData.value?.forEach { arrayOfPurchases.addAll(it.purchases) }

        val viewManager = LinearLayoutManager(activity)
        tableAdapter = RecycleViewAdapterGlobal(arrayOfPurchases)
        binding.recyclerViewAllData.apply {
            layoutManager = viewManager
            adapter = tableAdapter
            setHasFixedSize(true)
        }
    }

    private fun goToPurchaseList(widget : MaterialCalendarView, date : CalendarDay) {
        if (date.month - 1 == currentMonth && date.year == currentYear) {
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
                            createNewData(date)
                            //dialog.cancel()
                        }
                    }
                    .setNegativeButton("No", null)
                builder.show()
            }
        }
    }

    private fun createNewData(date : CalendarDay) {
        currentYear = date.year
        currentMonth = date.month - 1
        appSettings.edit().putInt(PREF_MONTH, currentMonth).putInt(PREF_YEAR, currentYear).apply()

        val result = mutableListOf<Table>()
        val table = Table(0, arrayListOf())

        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(currentYear, currentMonth, 1)
        val num = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..num) {
            result.add(i - 1, table)
        }

        tableViewModel.clearData()
        tableViewModel.insertAllData(result)
    }
}