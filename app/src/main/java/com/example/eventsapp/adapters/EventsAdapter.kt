package com.example.eventsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventsapp.R
import com.example.eventsapp.models.Attraction

class EventsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EVENT = 1
        private const val VIEW_TYPE_ANOTHER = 2
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView = itemView.findViewById(R.id.eventImage)
        val eventLocale: TextView = itemView.findViewById(R.id.eventLocale)
        val eventTitle: TextView = itemView.findViewById(R.id.eventTitle)
        val eventGenre: TextView = itemView.findViewById(R.id.eventGenre)
        val eventSubGenre: TextView = itemView.findViewById(R.id.eventSubGenre)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(differ.currentList[position])
                }
            }
        }
    }

    inner class AnotherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val anothereventImage: ImageView = itemView.findViewById(R.id.eventImage1)
        val anothereventTitle: TextView = itemView.findViewById(R.id.eventTitle1)
        val anothereventGenre: TextView = itemView.findViewById(R.id.eventGenre1)
        val anothereventSubGenre: TextView = itemView.findViewById(R.id.eventSubGenre1)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(differ.currentList[position])
                }
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Attraction>() {
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EVENT -> EventViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false)
            )
            VIEW_TYPE_ANOTHER -> AnotherViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_another, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                val event = differ.currentList[position]
                holder.apply {
                    Glide.with(itemView).load(event.images[4].url).into(eventImage)
                    eventLocale.text = event.locale
                    eventTitle.text = event.name
                    eventGenre.text = event.classifications.firstOrNull()?.genre?.name ?: "Unknown Genre"
                    eventSubGenre.text = event.classifications.firstOrNull()?.subGenre?.name ?: "Unknown SubGenre"
                }
            }
            is AnotherViewHolder -> {
                val event = differ.currentList[position]
                holder.apply {
                    Glide.with(itemView).load(event.images.last().url).into(anothereventImage)
                    anothereventTitle.text = event.name
                    anothereventGenre.text = event.classifications.firstOrNull()?.genre?.name ?: "Unknown Genre"
                    anothereventSubGenre.text = event.classifications.firstOrNull()?.subGenre?.name ?: "Unknown SubGenre"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            VIEW_TYPE_EVENT
        } else {
            VIEW_TYPE_ANOTHER
        }
    }

    fun setOnItemClickListener(listener: (Attraction) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemClickListener: ((Attraction) -> Unit)? = null
}
