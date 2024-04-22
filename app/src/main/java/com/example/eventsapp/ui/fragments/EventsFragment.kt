package com.example.eventsapp.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentEventsBinding
import com.example.eventsapp.ui.EventsActivity
import com.example.eventsapp.viewmodel.EventsViewModel
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
        //binding.eventName.text = event.name
        binding.collapsingToolbar.title=event.name
         binding.eventType.text=event.classifications.firstOrNull()?.segment?.name?:"Unknown Type"
         binding.eventGenre.text=event.classifications.firstOrNull()?.genre?.name?:"Unknown Genre"
         binding.eventSubgenre.text=event.classifications.firstOrNull()?.subGenre?.name?:"Unknown SubGenre"
//        binding.eventLocale.text=event.locale


        Glide.with(this)
            .load(event.images.first().url)
            .into(binding.eventImage)
//        Glide.with(this)
//            .load(event.images[1].url)
//            .into(binding.eventImage2)

        binding.wishlist.setOnClickListener{
            binding.wishlist.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            eventsViewModel.addToHistory(event)
            Snackbar.make(view,"Added to Wishlist",Snackbar.LENGTH_SHORT).show()

        }

    }



}