package com.example.climaapp.network

data class WeatherProperty(
        val id: Double,
        val name: String,
        val weather: List<WeatherData>,
        val main: MainData
)

data class WeatherData(
        val id: Double,
        val main: String
)

data class MainData(
        val temp: Double
)