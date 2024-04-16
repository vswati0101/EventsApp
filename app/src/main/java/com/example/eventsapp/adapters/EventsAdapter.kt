package com.example.eventsapp.adapters

import android.media.metrics.Event
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
import com.example.eventsapp.models.Genre

class EventsAdapter:RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    inner class EventViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    lateinit var eventImage:ImageView
    lateinit var eventLocale: TextView
    lateinit var eventTitle:TextView
    lateinit var eventGenre:TextView
    lateinit var eventSubGenre:TextView
    private val differCallback=object:DiffUtil.ItemCallback<Attraction>(){
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_events,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener:((Attraction)->Unit)?=null

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event=differ.currentList[position]
        eventImage=holder.itemView.findViewById(R.id.eventImage)
        eventLocale=holder.itemView.findViewById(R.id.eventLocale)
        eventTitle=holder.itemView.findViewById(R.id.eventTitle)
        eventGenre=holder.itemView.findViewById(R.id.eventGenre)
        eventSubGenre=holder.itemView.findViewById(R.id.eventSubGenre)
        holder.itemView.apply{
            Glide.with(this).load(event.images.first().url).into(eventImage)
            eventLocale.text=event.locale
            eventTitle.text=event.name
            eventGenre.text=event.classifications.firstOrNull()?.genre?.name?:"Unknown Genre"
            eventSubGenre.text=event.classifications.firstOrNull()?.subGenre?.name?:"Unknown SubGenre"
            setOnClickListener{
                onItemClickListener?.let {
                    it(event)
                }
            }
        }

    }
    fun setOnItemClickListener(listener:(Attraction)->Unit){
        onItemClickListener=listener
    }

}