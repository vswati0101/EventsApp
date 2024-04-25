package com.example.eventsapp.ui

import EventsViewModel
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.ActivityEventsBinding
import com.example.eventsapp.db.EventDatabase
import com.example.eventsapp.repository.EventsRepository
import com.example.eventsapp.viewmodel.EventsViewModelProviderFactory

class EventsActivity : AppCompatActivity() {
    lateinit var eventsViewModel: EventsViewModel
    lateinit var binding: ActivityEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val eventsRepository = EventsRepository(EventDatabase(this))
        val viewModelProviderFactory = EventsViewModelProviderFactory(application, eventsRepository)
        eventsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(EventsViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.eventsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


    }
    fun bottomNavVisibility(visible: Boolean) {
        if (visible) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

}