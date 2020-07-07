package com.example.climaapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [WeatherData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract val weatherDatabaseDao: WeatherDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            WeatherDatabase::class.java,
                            "weather_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}