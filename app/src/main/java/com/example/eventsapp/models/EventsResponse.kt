package com.example.eventsapp.models

data class EventsResponse(
    val _embedded: Embedded,
    val _links: LinksX,
    val page: Page
)