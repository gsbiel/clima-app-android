package com.example.climaapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.climaapp.ClimaData
import com.example.climaapp.database.WeatherDatabase
import com.example.climaapp.database.asDomainModel
import com.example.climaapp.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val database: WeatherDatabase){

    val weather: LiveData<ClimaData> =  Transformations.map(database.weatherDatabaseDao.getCache()){
        it.asDomainModel()
    }

    suspend fun refreshWeather(){
        withContext(Dispatchers.IO){
//            val clima = WeatherApi.retrofitService.getWeatherDataByGeolocation()
            // Preciso fazer a requisição com base na geolocalização. Mas como acessar os dados lat e long??
        }
    }
}