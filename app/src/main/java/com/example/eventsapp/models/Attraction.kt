package com.example.eventsapp.models

import android.os.Parcel
import android.os.Parcelable
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
    @PrimaryKey
    val id: String,
    val images: MutableList<Image>,
    val locale: String,
    val name: String,
    val test: Boolean,
    val type: String,
    val upcomingEvents: UpcomingEvents,
    val url: String
):Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("_links"),
        TODO("classifications"),
        TODO("externalLinks"),
        parcel.readString().toString(),
        TODO("images"),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        TODO("upcomingEvents"),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(locale)
        parcel.writeString(name)
        parcel.writeByte(if (test) 1 else 0)
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attraction> {
        override fun createFromParcel(parcel: Parcel): Attraction {
            return Attraction(parcel)
        }

        override fun newArray(size: Int): Array<Attraction?> {
            return arrayOfNulls(size)
        }
    }

}