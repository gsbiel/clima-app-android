package com.example.climaapp.clima

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.climaapp.R

@BindingAdapter("cloudIcon")
fun ImageView.setCloudImage(type: WeatherType){
    setImageResource(
            when(type){
                WeatherType.CLOUD -> R.drawable.ic_cloud
                WeatherType.CLOUD_FOG -> R.drawable.ic_cloud_fog
                WeatherType.CLOUD_BOLT -> R.drawable.ic_cloud_bolt
                WeatherType.CLOUD_DRIZZLE -> R.drawable.ic_cloud_drizzle
                WeatherType.CLOUD_RAIN -> R.drawable.ic_cloud_rain
                WeatherType.CLOUD_SNOW -> R.drawable.ic_cloud_snow
                WeatherType.SUN -> R.drawable.ic_sun
                WeatherType.SNOW -> R.drawable.ic_snowflake
                WeatherType.RAIN -> R.drawable.ic_rain
                else -> R.drawable.ic_cloud
            }
    )
}

@BindingAdapter("permissionsGranted")
fun Button.setVisibility(flag: Boolean){
    if(flag){
        visibility = View.INVISIBLE
    }
    else{
        visibility = View.VISIBLE
    }
}