package com.ruslanpark.kakeiboapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
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
    var currentYear = 0
    var currentMonth = 0
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
        currentYear = appSettings.getInt(PREF_YEAR, 0)
        currentMonth = appSettings.getInt(PREF_MONTH, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentBinding.inflate(inflater, container, false)

        binding.editTextNumberSalary.setText(appSettings.getInt(PREF_SALARY, 0).toString())
        binding.editTextNumberSpending.setText(appSettings.getInt(PREF_SPENDING, 0).toString())

        val array = arrayOf(0, 0, 0, 0, 0)
        val table = tableViewModel.readAllData.value
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(currentYear, currentMonth - 1, 1)
        val num = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..num) {
            calendar.set(currentYear, currentMonth - 1, i)
            table?.get(i - 1)?.purchases?.forEach {
                array[calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) - 1] += it.second
            }
        }


        binding.textViewWeek1Number.text = array[0].toString()
        binding.textViewWeek1Number.setTextColor(setColor(array[0]))
        binding.textViewWeek2Number.text = array[1].toString()
        binding.textViewWeek2Number.setTextColor(setColor(array[1]))
        binding.textViewWeek3Number.text = array[2].toString()
        binding.textViewWeek3Number.setTextColor(setColor(array[2]))
        binding.textViewWeek4Number.text = array[3].toString()
        binding.textViewWeek4Number.setTextColor(setColor(array[3]))
        binding.textViewWeek5Number.text = array[4].toString()
        binding.textViewWeek5Number.setTextColor(setColor(array[4]))

        var sum = 0
        array.forEach {
            sum += it
        }
        onTextChanged(sum)

        binding.editTextNumberSpending.doOnTextChanged { text, start, before, count ->
            if (binding.editTextNumberSpending.text.isNotEmpty() && binding.editTextNumberSalary.text.isNotEmpty()) {
                appSettings.edit().putInt(PREF_SPENDING, binding.editTextNumberSpending.text.toString().toInt()).apply()
                onTextChanged(sum)
            }
        }
        binding.editTextNumberSalary.doOnTextChanged { text, start, before, count ->
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