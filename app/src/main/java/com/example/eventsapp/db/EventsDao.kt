package com.example.eventsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventsapp.models.Attraction

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(event:Attraction):Long

    @Query("SELECT * FROM events")
    fun getAllEvents():LiveData<List<Attraction>>

    @Delete
    suspend fun deleteEvent(event: Attraction)

}