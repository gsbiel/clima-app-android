package com.example.climaapp

import com.example.climaapp.clima.WeatherType

data class ClimaData(
        val cityName: String,
        val temperature: Double,
        val weatherType: WeatherType
)