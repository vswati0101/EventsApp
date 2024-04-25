package com.example.eventsapp.models

import android.os.Parcel
import android.os.Parcelable

data class Image(
    val fallback: Boolean,
    val height: Int,
    val ratio: String?,
    val url: String?,
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (fallback) 1 else 0)
        parcel.writeInt(height)
        parcel.writeString(ratio)
        parcel.writeString(url)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Image

        if (fallback != other.fallback) return false
        if (height != other.height) return false
        if (width != other.width) return false
        if (ratio != other.ratio) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fallback.hashCode()
        result = 31 * result + height
        result = 31 * result + (ratio?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + width
        return result
    }
}
