package com.example.eventsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.eventsapp.R
import com.example.eventsapp.models.Attraction
import com.example.eventsapp.viewholder.AnotherViewHolder
import com.example.eventsapp.viewholder.EventViewHolder

class EventsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_EVENT = 1
        private const val VIEW_TYPE_ANOTHER = 2
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
                LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false),differ,
                onItemClickListener
            )
            VIEW_TYPE_ANOTHER -> AnotherViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_another, parent, false),differ,
                onItemClickListener
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is AnotherViewHolder -> {
                holder.bind(differ.currentList[position])
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
