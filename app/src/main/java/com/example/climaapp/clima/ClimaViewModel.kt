package com.example.climaapp.clima

import android.util.Log
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

    private val _permissionsGranted = MutableLiveData<Boolean>()
    val permissionsGranted: LiveData<Boolean>
        get() = _permissionsGranted

    private val _refreshEvent = MutableLiveData<Boolean>()
    val refreshEvent: LiveData<Boolean>
        get() = _refreshEvent

    init{
        _temperature.value = 29.0
        _city.value = "Vitória"
        _weatherType.value = WeatherType.CLOUD_FOG
        _refreshEvent.value = false
    }

    fun onSendButtonPressed() {
        Log.i("ClimaViewModel", "Send Button pressed!")
        textInput.value = ""
    }

    fun refresh(){
        if(_city.value!!.length > 0){

        }else{
            _refreshEvent.value = true
        }
    }

    fun refreshEventCompleted(){
        _refreshEvent.value = false
    }
}