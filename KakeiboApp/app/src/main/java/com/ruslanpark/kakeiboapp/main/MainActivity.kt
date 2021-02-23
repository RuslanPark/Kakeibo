package com.ruslanpark.kakeiboapp.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.ruslanpark.kakeiboapp.R
import com.ruslanpark.kakeiboapp.databinding.ActivityMainBinding
import com.ruslanpark.kakeiboapp.viewmodel.TableViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var appSettings: SharedPreferences
    private lateinit var tableViewModel: TableViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tableViewModel = ViewModelProvider(this).get(TableViewModel::class.java)
        appSettings = getSharedPreferences("PREF", Context.MODE_PRIVATE)

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.calendar_fragment
        } else {
            binding.bottomNavigation.selectedItemId = savedInstanceState.getInt("lastFragment")
        }

        val navGraphIds = listOf(
            R.navigation.calendar_navigation_graph,
            R.navigation.assessment_navigation_graph,
            R.navigation.account_navigation_graph
        )

        val controller = binding.bottomNavigation.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        currentNavController = controller

        savedInstanceState?.let {
            binding.bottomNavigation.selectedItemId = it.getInt("lastFragment")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("lastFragment", binding.bottomNavigation.selectedItemId)
        super.onSaveInstanceState(outState)
    }

}