package com.example.climaapp.network

data class WeatherProperty(
        val id: Double,
        val name: String,
        val weather: List<WeatherDataProperty>,
        val main: MainData
)

data class WeatherDataProperty(
        val id: Double,
        val main: String
)

data class MainData(
        val temp: Double
)