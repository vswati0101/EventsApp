package com.example.eventsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentEventsBinding
import com.example.eventsapp.ui.EventsActivity
import com.example.eventsapp.ui.EventsViewModel
import com.google.android.material.snackbar.Snackbar


class EventsFragment : Fragment(R.layout.fragment_events) {
    lateinit var eventsViewModel: EventsViewModel
    val args:EventsFragmentArgs by navArgs()
    lateinit var binding:FragmentEventsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentEventsBinding.bind(view)
        eventsViewModel=(activity as EventsActivity).eventsViewModel
        val event=args.event
        binding.webView.apply{
            webViewClient= WebViewClient()
            event.url?.let{
                loadUrl(it)
            }
        }
        binding.book.setOnClickListener{
            eventsViewModel.addToHistory(event)
            Snackbar.make(view,"Added to History",Snackbar.LENGTH_SHORT).show()

        }
    }



}