package com.example.eventsapp.models

data class UpcomingEvents(
    val _filtered: Int,
    val _total: Int,
//    val mfx-de: Int?,
//    val mfx-nl: Int?,
//    val mfx-no: Int?,
    val ticketmaster: Int?,
    val ticketweb: Int?,
    val tmr: Int?
)