    package com.ruslanpark.kakeiboapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.ruslanpark.kakeiboapp.databinding.FragmentAssessmentBinding
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel
import java.util.*

class AssessmentFragment : Fragment() {

    private var _binding : FragmentAssessmentBinding? = null
    private val binding get() = _binding!!

    private val PREF = "settings"
    private val PREF_MONTH = "month"
    private val PREF_YEAR = "year"
    private val PREF_SALARY = "salary"
    private val PREF_SPENDING = "spending"
    private lateinit var appSettings: SharedPreferences
    private var currentYear = -1
    private var currentMonth = -1
    private lateinit var tableViewModel: TableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewModel = activity?.run {
            ViewModelProvider(this).get(TableViewModel::class.java)
        }!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appSettings = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        currentYear = appSettings.getInt(PREF_YEAR, -1)
        currentMonth = appSettings.getInt(PREF_MONTH, -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentBinding.inflate(inflater, container, false)

        if (currentYear == -1) {
            return binding.root
        }

        binding.editTextNumberSalary.setText(appSettings.getInt(PREF_SALARY, 0).toString())
        binding.editTextNumberSpending.setText(appSettings.getInt(PREF_SPENDING, 0).toString())

        val entries = arrayListOf<BarEntry>()
        val table = tableViewModel.readAllData.value
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, currentMonth, 1)
        calendar.firstDayOfWeek = Calendar.MONDAY
        val num = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        println(currentMonth)
        var sum = 0
        for (i in 1..num) {
            calendar.set(currentYear, currentMonth, i)
            //println("${currentYear} ${currentMonth} ${i} ${calendar.get(Calendar.DAY_OF_WEEK)}")
            table?.get(i - 1)?.purchases?.forEach {
                sum += it.second
            }
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || i == num) {
                entries.add(BarEntry(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH).toFloat(), sum.toFloat()))
                sum = 0
            }
        }

        binding.barChart.legend.isEnabled = false
        binding.barChart.description.text = "Week"

        binding.barChart.axisRight.isEnabled = false
        binding.barChart.axisLeft.setDrawZeroLine(true)
        binding.barChart.axisLeft.setDrawGridLines(false)
        binding.barChart.axisLeft.setDrawAxisLine(false)
        binding.barChart.axisLeft.setDrawLabels(false)

        binding.barChart.xAxis.setDrawGridLines(false)
        binding.barChart.xAxis.granularity = 1F
        binding.barChart.xAxis.textSize = 10F
        binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val set = BarDataSet(entries, "DataSet")
        set.color = Color.RED
        set.valueTextSize = 15F
        val data = BarData(set)
        binding.barChart.data = data
        binding.barChart.invalidate()

        sum = 0
        entries.forEach {
            sum += it.y.toInt()
        }
        onTextChanged(sum)

        binding.editTextNumberSpending.doOnTextChanged { _, _, _, _ ->
            if (binding.editTextNumberSpending.text.isNotEmpty() && binding.editTextNumberSalary.text.isNotEmpty()) {
                appSettings.edit().putInt(PREF_SPENDING, binding.editTextNumberSpending.text.toString().toInt()).apply()
                onTextChanged(sum)
            }
        }
        binding.editTextNumberSalary.doOnTextChanged { _, _, _, _ ->
            if (binding.editTextNumberSpending.text.isNotEmpty() && binding.editTextNumberSalary.text.isNotEmpty()) {
                appSettings.edit().putInt(PREF_SALARY, binding.editTextNumberSalary.text.toString().toInt()).apply()
                onTextChanged(sum)
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onTextChanged(sum : Int) {
        var res = binding.editTextNumberSalary.text.toString().toInt()
        res -= binding.editTextNumberSpending.text.toString().toInt()
        res += sum
        binding.textViewLeftNumber.text = res.toString()
        binding.textViewLeftNumber.setTextColor(setColor(res))

    }

    private fun setColor(x : Int) : Int {
        if (x < 0) {
            return Color.RED
        }
        return Color.GREEN
    }
}