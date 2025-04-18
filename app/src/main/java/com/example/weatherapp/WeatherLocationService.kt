package com.example.weatherapp

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import retrofit2.HttpException

class WeatherLocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val CHANNEL_ID = "WeatherLocationChannel"
        const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification("Fetching weather..."))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        requestLocationAndUpdateWeather()
        return START_STICKY
    }

    private fun requestLocationAndUpdateWeather() {
        // Get the last known location; you could also request location updates for a continuous update.
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                serviceScope.launch {
                    try {
                        // Here you might want to call an API that supports location-based requests.
                        // If your current weather API doesn’t support lat/lon,
                        // consider switching to an endpoint that does (or use the One Call API).
                        val weatherResponse = WeatherClient.apiService.getWeatherByCoordinates(
                            lat = location.latitude,
                            lon = location.longitude,
                            apiKey = "babe7dcacf665bbd77e94a56d31846f4",
                            units = "metric"
                        )
                        val contentText = "Temp: ${weatherResponse.main.temp.toInt()}° | ${weatherResponse.weather.firstOrNull()?.main ?: ""} | ${weatherResponse.name}"
                        updateNotification(contentText)
                    } catch (e: HttpException) {
                        updateNotification("Error fetching weather (HTTP ${e.code()})")
                    } catch (e: Exception) {
                        updateNotification("Error fetching weather")
                    }
                }
            } else {
                updateNotification("Location not available")
            }
        }
    }

    // Build a notification with the given content text.
    private fun buildNotification(content: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Current Weather")
            .setContentText(content)
            .setSmallIcon(R.drawable.sun) // Use your appropriate weather icon
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    // Update the persistent notification.
    private fun updateNotification(content: String) {
        val notification = buildNotification(content)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    // Create notification channel for devices running Android O and above.
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Weather Location Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
