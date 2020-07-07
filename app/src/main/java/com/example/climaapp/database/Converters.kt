package com.example.climaapp.database

import androidx.room.TypeConverter
import com.example.climaapp.clima.WeatherType

class Converters{
    @TypeConverter
    fun fromIntegerToWeatherType(value: Int?): WeatherType?{
        return value?.let{
            when(it){
                1 -> WeatherType.ERROR
                2 -> WeatherType.CLOUD
                3 -> WeatherType.RAIN
                4 -> WeatherType.SUN
                5 -> WeatherType.CLOUD_FOG
                6 -> WeatherType.CLOUD_SNOW
                7 -> WeatherType.CLOUD_RAIN
                8 -> WeatherType.CLOUD_DRIZZLE
                9 -> WeatherType.SNOW
                10 -> WeatherType.CLOUD_BOLT
                else -> WeatherType.ERROR
            }
        }
    }

    @TypeConverter
    fun fromWeatherTypeToInt(value: WeatherType?): Int? {
        return value?.let{
            when(it){
                WeatherType.ERROR -> 1
                WeatherType.CLOUD -> 2
                WeatherType.RAIN -> 3
                WeatherType.SUN -> 4
                WeatherType.CLOUD_FOG -> 5
                WeatherType.CLOUD_SNOW -> 6
                WeatherType.CLOUD_RAIN -> 7
                WeatherType.CLOUD_DRIZZLE -> 8
                WeatherType.SNOW -> 9
                WeatherType.CLOUD_BOLT -> 10
                else -> 1
            }
        }
    }
}