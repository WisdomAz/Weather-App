package com.example.weatherapp

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val city: City,
    val list: List<ForecastDay>
)

@Serializable
data class City(
    val name: String
)

@Serializable
data class ForecastDay(
    val dt: Long,
    val temp: Temperature,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherCondition>
)

@Serializable
data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double
)
