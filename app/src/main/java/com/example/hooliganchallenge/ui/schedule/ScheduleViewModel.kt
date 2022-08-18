package com.example.hooliganchallenge.ui.schedule

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hooliganchallenge.MainApplication
import com.example.hooliganchallenge.models.ScheduleResponse
import com.example.hooliganchallenge.repository.EventsRepository
import com.example.hooliganchallenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val repository: EventsRepository, app: Application) :
    AndroidViewModel(app) {

    val getSchedule: MutableLiveData<Resource<ScheduleResponse>> = MutableLiveData()
    var getScheduleResponse: ScheduleResponse? = null

    fun getSchedule() = viewModelScope.launch {
        safeGetEventsCall()
    }

    private suspend fun safeGetEventsCall() {
        getScheduleResponse = null
        getSchedule.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getSchedule()
                getSchedule.postValue(handleGetScheduleResponse(response))
            } else {
                getSchedule.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> getSchedule.postValue(Resource.Error("Network failure"))
                else -> getSchedule.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleGetScheduleResponse(response: Response<ScheduleResponse>): Resource<ScheduleResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (getScheduleResponse == null) {

                    getScheduleResponse = resultResponse
                }
                return Resource.Success(getScheduleResponse ?: resultResponse)
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
