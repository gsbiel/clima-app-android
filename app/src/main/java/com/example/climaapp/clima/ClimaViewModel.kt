package com.example.climaapp.clima

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

enum class WeatherType {
    CLOUD, CLOUD_BOLT, CLOUD_DRIZZLE, CLOUD_FOG, CLOUD_RAIN, CLOUD_SNOW, SUN, SNOW, RAIN
}

class ClimaViewModel: ViewModel(){

    private val _temperature = MutableLiveData<Double>()
    val temperature = Transformations.map(_temperature){
        "%.1f".format(it)
    }

    private val _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city

    val textInput = MutableLiveData<String>()

    private val _weatherType = MutableLiveData<WeatherType>()
    val weatherType: LiveData<WeatherType>
        get() = _weatherType

    init{
        _temperature.value = 29.0
        _city.value = "Vitória"
        _weatherType.value = WeatherType.CLOUD_FOG
    }

    fun refresh(){

    }
}