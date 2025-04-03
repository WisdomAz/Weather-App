package com.example.weatherapp

import retrofit2.Retrofit
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

object WeatherClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType())
        )
        .build()

    val apiService: WeatherApi = retrofit.create(WeatherApi::class.java)
}
