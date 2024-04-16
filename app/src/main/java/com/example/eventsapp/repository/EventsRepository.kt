package com.example.eventsapp.repository

import com.example.eventsapp.api.RetrofitInstance
import com.example.eventsapp.db.EventDatabase
import com.example.eventsapp.models.Attraction

class EventsRepository(val db:EventDatabase) {
    suspend fun getEvents(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getEvents(countryCode, pageNumber)
    suspend fun upsert(event: Attraction)=db.getEventDao().upsert(event)
    fun getAllEvents()=db.getEventDao().getAllEvents()
    suspend fun deleteEvent(event: Attraction)=db.getEventDao().deleteEvent(event)
}