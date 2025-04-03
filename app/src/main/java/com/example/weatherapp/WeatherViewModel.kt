package com.example.weatherapp

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class WeatherViewModel : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                Log.d("WeatherViewModel", "Fetching weather for $city...")
                val response = withContext(Dispatchers.IO) {
                    WeatherClient.apiService.getWeather(city, "babe7dcacf665bbd77e94a56d31846f4", "metric")
                }
                Log.d("WeatherViewModel", "Received weather: $response")
                _weather.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather", e)
                _weather.value = null
            }
        }
    }
}
