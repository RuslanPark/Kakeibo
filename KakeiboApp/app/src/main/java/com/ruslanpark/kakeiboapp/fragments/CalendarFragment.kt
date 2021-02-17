package com.ruslanpark.kakeiboapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.ruslanpark.kakeiboapp.databinding.FragmentAccountBinding
import com.ruslanpark.kakeiboapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.calendarView.topbarVisible = false;
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val action = CalendarFragmentDirections.actionCalendarFragmentToDayFinancesFragment()
            widget.findNavController().navigate(action)
        }
        
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}