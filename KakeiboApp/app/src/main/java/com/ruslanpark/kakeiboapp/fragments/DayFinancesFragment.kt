package com.ruslanpark.kakeiboapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruslanpark.kakeiboapp.R
import com.ruslanpark.kakeiboapp.adapter.RecycleViewAdapter
import com.ruslanpark.kakeiboapp.databinding.FragmentCalendarBinding
import com.ruslanpark.kakeiboapp.databinding.FragmentDayFinancesBinding
import com.ruslanpark.kakeiboapp.model.Table
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel

class DayFinancesFragment : Fragment() {

    private var _binding : FragmentDayFinancesBinding? = null
    private val binding get() = _binding!!
    private lateinit var tableViewModel: TableViewModel
    private lateinit var tablePurchases : Table//ArrayList< Pair<String, Int> > = arrayListOf()
    private val args: DayFinancesFragmentArgs by navArgs()
    private lateinit var tableAdapter: RecycleViewAdapter

    private fun showList() {
        val viewManager = LinearLayoutManager(activity)
        tableAdapter = RecycleViewAdapter(tablePurchases.purchases) {
            tablePurchases.purchases.removeAt(it)
            tableViewModel.insertData(tablePurchases)
        }
        if (tableAdapter.itemCount == 0) {
            Toast.makeText(activity, "No saved data, please refresh", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = tableAdapter
        }
    }

    private fun bindObservers() {
        tableViewModel.readAllData.observe(this, {
            tablePurchases = it[args.day - 1]
            if (it[args.day - 1].purchases.isEmpty() || !this::tableAdapter.isInitialized) {
                showList()
            } else {
                tableAdapter.update(tablePurchases.purchases)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewModel = ViewModelProvider(this).get(TableViewModel::class.java)
        bindObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDayFinancesBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            activity?.let {
                val linl = LinearLayout(requireContext())
                val purchaseEditTextField = EditText(requireActivity())
                purchaseEditTextField.hint = "Purchase"
                val descriptionEditTextField = EditText(requireActivity())
                descriptionEditTextField.hint = "Description"
                linl.gravity = LinearLayout.VERTICAL
                linl.addView(purchaseEditTextField)
                linl.addView(descriptionEditTextField)

                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Write purchase and description")
                    .setMessage(null)
                    .setView(linl)
                    .setPositiveButton("OK") { _, _ ->
                        val pairTable = Pair(descriptionEditTextField.text.toString(), purchaseEditTextField.text.toString().toInt())
                        tablePurchases.purchases.add(pairTable)
                        tableViewModel.insertData(tablePurchases)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}