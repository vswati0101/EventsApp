package com.example.eventsapp.ui.fragments

import android.content.Intent
import android.net.Uri
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
        binding.eventName.text = event.name
        binding.collapsingToolbar.title=event.name
        binding.eventType.text=event.classifications.firstOrNull()?.segment?.name?:"Unknown Type"
        binding.eventGenre.text=event.classifications.firstOrNull()?.genre?.name?:"Unknown Genre"
        binding.eventSubgenre.text=event.classifications.firstOrNull()?.subGenre?.name?:"Unknown SubGenre"
//        binding.eventLocale.text=event.locale


        Glide.with(this)
            .load(event.images.first().url)
            .into(binding.eventImage)
        Glide.with(this)
            .load(event.images.first().url)
            .into(binding.eventImage2)
        event.externalLinks?.facebook?.firstOrNull()?.url?.let { facebookLink ->
            if (facebookLink.isNotEmpty()) {
                binding.eventFacebook.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink))
                    startActivity(intent)
                }
            }
        }
        event.externalLinks?.instagram?.firstOrNull()?.url?.let { instagramLink ->
            if (instagramLink.isNotEmpty()) {
                binding.eventInstagram.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink))
                    startActivity(intent)
                }
            }
        }
        event.externalLinks?.youtube?.firstOrNull()?.url?.let { youtubeLink ->
            if (youtubeLink.isNotEmpty()) {
                binding.eventYoutube.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                    startActivity(intent)
                }
            }
        }
//        binding.eventFacebook.setOnClickListener {
//            val facebookLink = event.externalLinks?.facebook?.firstOrNull()?.url ?: ""
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink))
//            startActivity(intent)
//            }
//        binding.eventInstagram.setOnClickListener {
//            val instagramLink = event.externalLinks?.instagram?.firstOrNull()?.url ?: ""
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink))
//            startActivity(intent)
//        }
//        binding.eventYoutube.setOnClickListener {
//            val youtubeLink = event.externalLinks?.youtube?.firstOrNull()?.url ?: ""
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
//            startActivity(intent)
//        }

        binding.wishlist.setOnClickListener{
            eventsViewModel.addToHistory(event)
            Snackbar.make(view,"Added to Wishlist",Snackbar.LENGTH_SHORT).show()
        }
        (activity as EventsActivity).bottomNavVisibility(false)

    }
    override fun onResume() {
        super.onResume()
        (activity as EventsActivity).bottomNavVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as EventsActivity).bottomNavVisibility(true)
    }



}
