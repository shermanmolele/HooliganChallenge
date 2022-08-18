package com.example.hooliganchallenge.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hooliganchallenge.MainApplication
import com.example.hooliganchallenge.models.EventResponse
import com.example.hooliganchallenge.repository.EventsRepository
import com.example.hooliganchallenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: EventsRepository, app: Application) :
    AndroidViewModel(app) {

    val getEvents: MutableLiveData<Resource<EventResponse>> = MutableLiveData()
    var getEventsResponse: EventResponse? = null

    fun getEvents() = viewModelScope.launch {
        safeGetEventsCall()
    }

    fun getEvent(id: Int) = viewModelScope.launch {
        safeGetEventCall(id)
    }

    private suspend fun safeGetEventsCall() {
        getEventsResponse = null
        getEvents.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getEvents()
                getEvents.postValue(handleGetEventsResponse(response))
            } else {
                getEvents.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getEvents.postValue(Resource.Error("Network failure"))
                else -> getEvents.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeGetEventCall(id: Int) {
        getEventsResponse = null
        getEvents.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getEvent(id)
                getEvents.postValue(handleGetEventsResponse(response))
            } else {
                getEvents.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getEvents.postValue(Resource.Error("Network failure"))
                else -> getEvents.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleGetEventsResponse(response: Response<EventResponse>): Resource<EventResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (getEventsResponse == null) {

                    getEventsResponse = resultResponse
                }
                return Resource.Success(getEventsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MainApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}

