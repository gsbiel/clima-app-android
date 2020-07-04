package com.example.climaapp.clima

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.climaapp.BuildConfig
import com.example.climaapp.network.WeatherApi
import com.example.climaapp.network.WeatherProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class WeatherType {
    CLOUD, CLOUD_BOLT, CLOUD_DRIZZLE, CLOUD_FOG, CLOUD_RAIN, CLOUD_SNOW, SUN, SNOW, RAIN
}

class ClimaViewModel: ViewModel(){

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double>
        get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double>
        get() = _longitude

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

    private val _setPermissionEvent = MutableLiveData<Boolean>()
    val setPermissionEvent: LiveData<Boolean>
        get() = _setPermissionEvent

    init{
        _temperature.value = 29.0
        _city.value = "Vitória"
        _weatherType.value = WeatherType.CLOUD_FOG
        _refreshEvent.value = false
        _setPermissionEvent.value = false
        _permissionsGranted.value = true
    }

    fun onSendButtonPressed() {
        Log.i("ClimaViewModel", "Send Button pressed!")
        textInput.value = ""
        fetchWeatherDataBasedOnCityName()
    }

    fun onSetPermissionButtonPressed() {
        Log.i("ClimaViewModel", "Set Permission button pressed!")
        _setPermissionEvent.value = true
    }

    fun setLatitudeTo(value: Double){
        _latitude.value = value
    }

    fun setLongitudeTo(value: Double){
        _longitude.value = value
    }

    fun setCityNameTo(value: String){
        _city.value = value
    }


    fun setPermissionsTo(flag: Boolean){
        _permissionsGranted.value = flag
    }

    private fun fetchWeatherDataBasedOnCityName(){
        Log.i("ClimaViewModel", "Fetch data for city: ${_city.value}")
        WeatherApi.retrofitService.getWeatherDataByCityName(
                _city.value!!,
                BuildConfig.WEATHER_API_KEY
        ).enqueue(object : Callback<WeatherProperty> {
            override fun onFailure(call: Call<WeatherProperty>, t: Throwable) {
                Log.i("ClimaViewModel", "Erro na requisição :(. Erro: ${t.message}")
            }

            override fun onResponse(call: Call<WeatherProperty>, response: Response<WeatherProperty>) {
                Log.i("ClimaViewModel", "Requisição bem sucedida. Resposta: $response")
                _weatherType.value = response.body()?.weather?.get(0)?.id?.toInt()?.let { getWeatherTypeFor(it) }
                _city.value = response.body()?.name
                _temperature.value = response.body()?.main?.temp?.minus(273)
            }

        })
    }

    fun fetchWeatherDataBasedOnLatLongEntry(){
        Log.i("ClimaViewModel", "Fetch data for lat: ${_latitude.value} and long: ${_longitude.value}")
        WeatherApi.retrofitService.getWeatherDataByGeolocation(
                _latitude.value!!.toString(),
                _longitude.value!!.toString(),
                BuildConfig.WEATHER_API_KEY
        ).enqueue(object : Callback<WeatherProperty> {

            override fun onFailure(call: Call<WeatherProperty>, t: Throwable) {
                Log.i("ClimaViewModel", "Erro na requisição :(. Erro: ${t.message}")
            }

            override fun onResponse(call: Call<WeatherProperty>, response: Response<WeatherProperty>) {
                _weatherType.value = response.body()?.weather?.get(0)?.id?.toInt()?.let { getWeatherTypeFor(it) }
                _city.value = response.body()?.name
                _temperature.value = response.body()?.main?.temp?.minus(273)
            }
        })
    }

    private fun getWeatherTypeFor(id: Int): WeatherType{
        return when(id){
            in 200..232 -> WeatherType.RAIN
            in 300..321 -> WeatherType.CLOUD_DRIZZLE
            in 500..531 -> WeatherType.CLOUD_RAIN
            in 600..622 -> WeatherType.CLOUD_SNOW
            in 701..781 -> WeatherType.CLOUD_FOG
            in 800..800 -> WeatherType.SUN
            in 801..804 -> WeatherType.RAIN
            else -> WeatherType.CLOUD
        }
    }

    fun refresh(){
        _refreshEvent.value = true
        if(_permissionsGranted.value!!){
            Log.i("ClimaViewModel", "Refresh is Authorized")
        }
    }

    fun refreshEventCompleted(){
        _refreshEvent.value = false
    }

    fun setPermissionEventCompleted(){
        _setPermissionEvent.value = false
    }
}