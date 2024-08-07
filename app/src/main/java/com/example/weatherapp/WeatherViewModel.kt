package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather
    private val apiKey = "b2fbcb88a4da04b923fdbc90b4e07481"

    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getCurrentWeather(cityName, "metric", apiKey)
            if (response.isSuccessful) {
                _weather.value = response.body()
            }
        }
    }
}
