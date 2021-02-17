package com.ruslanpark.kakeiboapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruslanpark.kakeiboapp.R
import com.ruslanpark.kakeiboapp.adapter.RecycleViewAdapter
import com.ruslanpark.kakeiboapp.databinding.FragmentCalendarBinding
import com.ruslanpark.kakeiboapp.databinding.FragmentDayFinancesBinding

class DayFinancesFragment : Fragment() {

    private var _binding : FragmentDayFinancesBinding? = null
    private val binding get() = _binding!!

    private var listOfValues = mutableListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDayFinancesBinding.inflate(inflater, container, false)


        val viewManager = LinearLayoutManager(activity)
        val valueAdapter = RecycleViewAdapter(listOfValues) {}
        if (valueAdapter.itemCount == 0) {
            Toast.makeText(activity, "No saved data, please refresh", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = valueAdapter
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}