package com.example.climaapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.climaapp.clima.WeatherType

@Entity(tableName = "weather_data_cache")
data class WeatherData(
        @PrimaryKey(autoGenerate = true)
        var weatherId: Long = 0L,

        @ColumnInfo(name="data_type")
        var dataType: String,

        @ColumnInfo(name="weather_type")
        var weatherType: WeatherType,

        @ColumnInfo(name="weather_temperature")
        var weatherTemperature: Double,

        @ColumnInfo(name="weather_city_name")
        var weatherCityName: String
)