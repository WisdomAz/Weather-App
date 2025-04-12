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
        @Query("zip") zip: String,  
        @Query("appid") apiKey: String,
        @Query("cnt") count: Int = 16,
        @Query("units") units: String = "metric"
    ): ForecastResponse
}
