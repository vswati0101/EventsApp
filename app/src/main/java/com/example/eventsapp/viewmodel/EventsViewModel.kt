import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventsapp.models.Attraction
import com.example.eventsapp.models.EventsResponse
import com.example.eventsapp.repository.EventsRepository
import com.example.eventsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class EventsViewModel(app: Application, val eventsRepository: EventsRepository) :
    AndroidViewModel(app) {

    val event: MutableLiveData<Resource<EventsResponse>> = MutableLiveData()
    var eventsPage = 1
    var eventsResponse: EventsResponse? = null

    init {
        getEvents("us")
    }

    fun getEvents(countryCode: String) = viewModelScope.launch {
        eventsInternet(countryCode)
    }

    fun addToHistory(event: Attraction) = viewModelScope.launch {
        if (!isEventInWishlist(event)) { // Check if the event is not already in the wishlist
            eventsRepository.upsert(event)
        }
    }

    fun removeFromHistory(event: Attraction) = viewModelScope.launch {
        eventsRepository.deleteEvent(event)
    }

    suspend fun isEventInWishlist(event: Attraction): Boolean {
        return eventsRepository.isEventInWishlist(event)
    }

    fun getTickets() = eventsRepository.getAllEvents()

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }

            } ?: false
        }
    }

    private suspend fun eventsInternet(countryCode: String) {
        event.postValue(Resource.Loading())
        try {
            if (internetConnection(getApplication())) {
                val response = eventsRepository.getEvents(countryCode, eventsPage)
                event.postValue(handleEventsResponse(response))
            } else {
                event.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> event.postValue(Resource.Error("Unable to connect"))
                else -> event.postValue(Resource.Error("No signal"))
            }
        }
    }

    private fun handleEventsResponse(response: Response<EventsResponse>): Resource<EventsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                eventsPage++
                if (eventsResponse == null) {
                    eventsResponse = resultResponse
                } else {
                    // Add logic here if needed
                }
                return Resource.Success(eventsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
