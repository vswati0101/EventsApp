package com.example.eventsapp.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.adapters.WishlistAdapter
import com.example.eventsapp.models.Attraction

class WishlistViewHolder(
    itemView: View,
    private val differ: WishlistAdapter,
    private val onItemClickListener: ((Attraction) -> Unit)?
) : RecyclerView.ViewHolder(itemView) {
    private val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
    private val eventTitle: TextView = itemView.findViewById(R.id.eventTitle)
    private val eventGenre: TextView = itemView.findViewById(R.id.eventGenre)
    private val eventSubGenre: TextView = itemView.findViewById(R.id.eventSubGenre)
    private val eventLocale: TextView = itemView.findViewById(R.id.eventLocale)

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.invoke(differ.differ.currentList[position])
            }
        }
    }
    fun bind(event: Attraction) {
        Glide.with(itemView).load(event.images[0].url).into(eventImage)
        eventTitle.text = event.name
        eventGenre.text = event.classifications.firstOrNull()?.genre?.name ?: "Unknown Genre"
        eventSubGenre.text = event.classifications.firstOrNull()?.subGenre?.name ?: "Unknown SubGenre"
        eventLocale.text = event.locale
    }
}
