package com.example.eventsapp.db

import androidx.room.TypeConverter
import com.example.eventsapp.models.Classification
import com.example.eventsapp.models.ExternalLinks
import com.example.eventsapp.models.Image
import com.example.eventsapp.models.Links
import com.example.eventsapp.models.UpcomingEvents
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LinksConverter {
    @TypeConverter
    fun fromLinks(links: Links): String {
        return Gson().toJson(links)
    }

    @TypeConverter
    fun toLinks(json: String): Links {
        val type = object : TypeToken<Links>() {}.type
        return Gson().fromJson(json, type)
    }
}

class ClassificationConverter {
    @TypeConverter
    fun fromClassifications(classifications: MutableList<Classification>): String {
        return Gson().toJson(classifications)
    }

    @TypeConverter
    fun toClassifications(json: String): MutableList<Classification> {
        val type = object : TypeToken<MutableList<Classification>>() {}.type
        return Gson().fromJson(json, type)
    }
}

class ExternalLinksConverter {
    @TypeConverter
    fun fromExternalLinks(externalLinks: ExternalLinks): String {
        return Gson().toJson(externalLinks)
    }

    @TypeConverter
    fun toExternalLinks(json: String): ExternalLinks {
        val type = object : TypeToken<ExternalLinks>() {}.type
        return Gson().fromJson(json, type)
    }
}

class ImageConverter {
    @TypeConverter
    fun fromImages(images: MutableList<Image>): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImages(json: String): MutableList<Image> {
        val type = object : TypeToken<MutableList<Image>>() {}.type
        return Gson().fromJson(json, type)
    }
}

class UpcomingEventsConverter {
    @TypeConverter
    fun fromUpcomingEvents(upcomingEvents: UpcomingEvents): String {
        return Gson().toJson(upcomingEvents)
    }

    @TypeConverter
    fun toUpcomingEvents(json: String): UpcomingEvents {
        val type = object : TypeToken<UpcomingEvents>() {}.type
        return Gson().fromJson(json, type)
    }
}