package com.example.eventsapp.viewmodel

import EventsViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventsapp.repository.EventsRepository

class EventsViewModelProviderFactory(val app:Application,val eventsRepository: EventsRepository):ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelCLass:Class<T>):T{
        return EventsViewModel(app,eventsRepository) as T
    }
}