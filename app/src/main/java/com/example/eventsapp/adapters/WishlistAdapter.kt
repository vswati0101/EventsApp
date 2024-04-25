package com.example.eventsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.eventsapp.R
import com.example.eventsapp.models.Attraction
import com.example.eventsapp.viewholder.WishlistViewHolder

class WishlistAdapter : RecyclerView.Adapter<WishlistViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Attraction>() {
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_events, parent, false)
        return WishlistViewHolder(view, this, onItemClickListener)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setOnItemClickListener(listener: (Attraction) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemClickListener: ((Attraction) -> Unit)? = null
}
