package com.example.eventsapp.ui.fragments

import EventsViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentEventsBinding
import com.example.eventsapp.models.Attraction
import com.example.eventsapp.ui.EventsActivity
import com.google.android.material.snackbar.Snackbar

class EventsFragment : Fragment(R.layout.fragment_events) {
    lateinit var eventsViewModel: EventsViewModel
    private lateinit var event: Attraction
    val args: EventsFragmentArgs by navArgs()
    lateinit var binding: FragmentEventsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentEventsBinding.bind(view)
        val event = args.event
        super.onViewCreated(view, savedInstanceState)
        var isEventInWishlist = true
        updateWishlistButtonColor(isEventInWishlist)
        binding.wishlist.setOnClickListener {
            if (isEventInWishlist) {
                eventsViewModel.removeFromHistory(event)
                Snackbar.make(view, "Removed from Wishlist", Snackbar.LENGTH_SHORT).show()
            } else {
                eventsViewModel.addToHistory(event)
                Snackbar.make(view, "Added to Wishlist", Snackbar.LENGTH_SHORT).show()
            }
            isEventInWishlist = !isEventInWishlist
            updateWishlistButtonColor(isEventInWishlist)
        }
        eventsViewModel = (activity as EventsActivity).eventsViewModel
        binding.eventName.text = event.name
        binding.collapsingToolbar.title = event.name
        binding.eventType.text =
            event.classifications.firstOrNull()?.segment?.name ?: "Unknown Type"
        binding.eventGenre.text =
            event.classifications.firstOrNull()?.genre?.name ?: "Unknown Genre"
        binding.eventSubgenre.text =
            event.classifications.firstOrNull()?.subGenre?.name ?: "Unknown SubGenre"
        Glide.with(this).load(event.images.first().url).into(binding.eventImage)
        Glide.with(this).load(event.images.first().url).into(binding.eventImage2)
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

    private fun updateWishlistButtonColor(isInWishlist: Boolean) {
        if (isInWishlist) {
            binding.wishlist.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.gray
                )
            )
        } else {
            binding.wishlist.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.pink
                )
            )
        }
    }

}
