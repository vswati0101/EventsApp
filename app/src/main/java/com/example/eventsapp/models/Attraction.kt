package com.example.eventsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "events"
)
data class Attraction(
    val _links: Links,
    val classifications: MutableList<Classification>,
    val externalLinks: ExternalLinks?,
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val images: MutableList<Image>,
    val locale: String,
    val name: String,
    val test: Boolean,
    val type: String,
    val upcomingEvents: UpcomingEvents,
    val url: String
):Serializable