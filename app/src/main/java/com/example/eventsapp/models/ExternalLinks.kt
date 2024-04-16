package com.example.eventsapp.models

data class ExternalLinks(
    val facebook: MutableList<Facebook>?,
    val homepage: MutableList<Homepage>?,
    val instagram: MutableList<Instagram>?,
    val itunes: MutableList<Itune>?,
    val musicbrainz: MutableList<Musicbrainz>?,
    val spotify: MutableList<Spotify>?,
    val twitter: MutableList<Twitter>?,
    val wiki: MutableList<Wiki>?,
    val youtube: MutableList<Youtube>?
)