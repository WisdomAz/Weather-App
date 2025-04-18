package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,  // For example: "12345,us"
        @Query("appid") apiKey: String,
        @Query("cnt") count: Int = 16,
        @Query("units") units: String = "metric"
    ): ForecastResponse

    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

}
