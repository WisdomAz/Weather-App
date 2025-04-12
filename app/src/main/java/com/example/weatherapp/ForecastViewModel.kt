package com.example.weatherapp

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class ForecastViewModel : ViewModel() {
    private val _forecast = MutableLiveData<ForecastResponse?>()
    val forecast: LiveData<ForecastResponse?> = _forecast

    fun fetchForecast(zip: String) {
        viewModelScope.launch {
            try {
                Log.d("ForecastViewModel", "Fetching forecast for $zip")

                val response = withContext(Dispatchers.IO) {
                    WeatherClient.apiService.getForecast("$zip,us", "babe7dcacf665bbd77e94a56d31846f4", count = 16, units = "metric")
                }
                Log.d("ForecastViewModel", "Received forecast: $response")
                _forecast.value = response
            } catch (e: Exception) {
                Log.e("ForecastViewModel", "Error fetching forecast", e)
                _forecast.value = null
            }
        }
    }
}
