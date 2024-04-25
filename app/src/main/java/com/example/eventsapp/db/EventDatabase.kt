package com.example.eventsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventsapp.models.Attraction

@Database(
    entities = [Attraction::class], version = 1
)
@TypeConverters(
    LinksConverter::class,
    ClassificationConverter::class,
    ExternalLinksConverter::class,
    ImageConverter::class,
    UpcomingEventsConverter::class
)
abstract class EventDatabase : RoomDatabase() {
    abstract fun getEventDao(): EventsDao
    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }
        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, EventDatabase::class.java, "event_db.db"
        ).build()
    }
}