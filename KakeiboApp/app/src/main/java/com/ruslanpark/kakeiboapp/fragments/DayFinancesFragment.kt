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
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruslanpark.kakeiboapp.adapter.RecycleViewAdapter
import com.ruslanpark.kakeiboapp.databinding.FragmentDayFinancesBinding
import com.ruslanpark.kakeiboapp.model.Table
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel

class DayFinancesFragment : Fragment() {

    private var _binding: FragmentDayFinancesBinding? = null
    private val binding get() = _binding!!

    private val args: DayFinancesFragmentArgs by navArgs()
    private lateinit var tableViewModel: TableViewModel
    private lateinit var tablePurchases: Table
    private lateinit var tableAdapter: RecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableViewModel = activity?.run {
            ViewModelProvider(this).get(TableViewModel::class.java)
        }!!
        tablePurchases = tableViewModel.readAllData.value!![args.day - 1]
        bindObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayFinancesBinding.inflate(inflater, container, false)

        showList()
        binding.floatingActionButton.setOnClickListener {
            doFloatingActionButton(it)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showList() {
        val viewManager = LinearLayoutManager(activity)
        tableAdapter = RecycleViewAdapter(tablePurchases.purchases) {
            tablePurchases.purchases.removeAt(it)
            tableViewModel.insertData(tablePurchases)
        }
        binding.recyclerViewDailyData.apply {
            layoutManager = viewManager
            adapter = tableAdapter
        }
    }

    private fun bindObservers() {
        tableViewModel.readAllData.observe(this, {
            tablePurchases = it[args.day - 1]
            tableAdapter.update(tablePurchases.purchases)
        })
    }

    private fun doFloatingActionButton(view: View) {
        activity?.let {

            val linearLayout = LinearLayout(requireContext()).also {
                it.gravity = LinearLayout.VERTICAL

                it.addView(EditText(requireActivity()).also { it1 ->
                    it1.hint = "Purchase"
                }, 0)

                it.addView(EditText(requireActivity()).also { it1 ->
                    it1.hint = "Description"
                }, 1)
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Write purchase and description")
                .setMessage(null)
                .setView(linearLayout)
                .setPositiveButton("OK") { _, _ ->
                    tablePurchases.purchases.add(
                        Pair(
                            (linearLayout[1] as EditText).text.toString(),
                            (linearLayout[0] as EditText).text.toString().toInt()
                        )
                    )
                    tableViewModel.insertData(tablePurchases)
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }
    }
}