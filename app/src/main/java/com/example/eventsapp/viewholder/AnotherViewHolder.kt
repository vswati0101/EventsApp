package com.example.eventsapp.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.models.Attraction

class AnotherViewHolder(itemView: View, private val differ: AsyncListDiffer<Attraction>,private val onItemClickListener: ((Attraction) -> Unit)?) :
    RecyclerView.ViewHolder(itemView) {
    private val anotherEventImage: ImageView = itemView.findViewById(R.id.eventImage1)
    private val anotherEventTitle: TextView = itemView.findViewById(R.id.eventTitle1)
    private val anotherEventGenre: TextView = itemView.findViewById(R.id.eventGenre1)
    private val anotherEventSubGenre: TextView = itemView.findViewById(R.id.eventSubGenre1)

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.invoke(differ.currentList[position])
            }
        }
    }

    fun bind(event: Attraction) {
        Glide.with(itemView).load(event.images.last().url).into(anotherEventImage)
        anotherEventTitle.text = event.name
        anotherEventGenre.text = event.classifications.firstOrNull()?.genre?.name ?: "Unknown Genre"
        anotherEventSubGenre.text = event.classifications.firstOrNull()?.subGenre?.name ?: "Unknown SubGenre"
    }
}
