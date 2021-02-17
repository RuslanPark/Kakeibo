package com.ruslanpark.kakeiboapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.ruslanpark.kakeiboapp.databinding.ActivityMainBinding
import com.ruslanpark.kakeiboapp.fragments.CalendarFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null

    private val monthArray = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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