package com.example.climaapp.database

import androidx.room.*

@Dao
interface WeatherDatabaseDao{
    @Update
    fun update(weatherData: WeatherData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherData: WeatherData)

    @Query("SELECT * FROM weather_data_cache WHERE data_type = 'cache'")
    fun getCache(): WeatherData?
}