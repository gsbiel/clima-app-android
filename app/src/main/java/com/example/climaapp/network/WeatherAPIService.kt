package com.example.climaapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

// POR COORDENADAS
//"lat={lat}&lon={lon}&appid={your api key}"

// POR NOME DA CIDADE
//"q={city name}&appid={your api key}"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface WeatherAPIService{
    @GET("weather")
    fun getWeatherDataByGeolocation(
            @Query("lat") latitude: String,
            @Query("lon") longitude: String,
            @Query("appid") apiKey: String
    ):
            Call<WeatherProperty>

    @GET("weather")
    fun getWeatherDataByCityName(
            @Query("q") cityName: String,
            @Query("appid") apiKey: String
    ):
            Call<WeatherProperty>
}

object WeatherApi {
    val retrofitService: WeatherAPIService by lazy {
        retrofit.create(WeatherAPIService::class.java)
    }
}