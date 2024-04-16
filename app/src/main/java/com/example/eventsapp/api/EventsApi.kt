package com.example.eventsapp.api

import com.example.eventsapp.models.EventsResponse
import com.example.eventsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsApi {
    @GET("/attractions")
    suspend fun getEvents(
        @Query("country")
        countryCode:String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ): Response<EventsResponse>

}