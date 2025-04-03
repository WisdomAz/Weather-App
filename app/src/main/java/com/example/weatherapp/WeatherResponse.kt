package com.example.weatherapp

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<WeatherCondition>,
    val main: MainWeather,
    val wind: Wind,
    val name: String
)

@Serializable
data class WeatherCondition(
    val main: String,
    val description: String
)

@Serializable
data class MainWeather(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
    val pressure: Int
)

@Serializable
data class Wind(
    val speed: Double
)
