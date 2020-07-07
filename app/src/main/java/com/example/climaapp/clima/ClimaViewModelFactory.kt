package com.example.climaapp.clima

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.climaapp.database.WeatherDatabaseDao

class ClimaViewModelFactory(
        private val databaseDao: WeatherDatabaseDao,
        private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClimaViewModel::class.java)){
            return ClimaViewModel(databaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}