package com.example.eventsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.eventsapp.models.Attraction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.models.EventsResponse
import com.example.eventsapp.repository.EventsRepository
import com.example.eventsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.util.Locale.IsoCountryCode

class EventsViewModel(app:Application,val eventsRepository: EventsRepository) :AndroidViewModel(app){
    val event:MutableLiveData<Resource<EventsResponse>> = MutableLiveData()
    var eventsPage=1
    var eventsResponse:EventsResponse?=null
    init{
        getEvents("us")
    }
    fun getEvents(countryCode: String)=viewModelScope.launch{
        eventsInternet(countryCode)
    }
    private fun handleEventsResponse(response: Response<EventsResponse>):Resource<EventsResponse>{
        if(response.isSuccessful){
            response.body()?.let{ resultResponse->
                eventsPage++
                if(eventsResponse==null){
                    eventsResponse=resultResponse
                } else{
//                    val oldEvents=eventsResponse?.events
//                    val newEvents=resultResponse.events
//                    oldEvents?.addAll(newEvents)
                }
                return Resource.Success(eventsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())

    }
    fun addToHistory(event: Attraction)=viewModelScope.launch {
        eventsRepository.upsert(event)
    }
    fun getTickets() = eventsRepository.getAllEvents()
    fun deleteEvent(event: Attraction)=viewModelScope.launch {
        eventsRepository.deleteEvent(event)
    }
    fun internetConnection(context: Context):Boolean{
        ( context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply{
            return getNetworkCapabilities(activeNetwork)?.run{
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->true
                    else->false
                }

            }?:false
        }
    }
    private suspend fun eventsInternet(countryCode:String){
        event.postValue(Resource.Loading())
        try{
            if(internetConnection(this.getApplication())){
                val response=eventsRepository.getEvents(countryCode,eventsPage)
                event.postValue(handleEventsResponse(response))

            } else{
                event.postValue(Resource.Error("No internet connection"))
            }

        } catch(t:Throwable){
            when(t){
                is IOException->event.postValue(Resource.Error("Unable to connect"))
                else-> event.postValue(Resource.Error("NO signal"))
            }
        }
    }

}