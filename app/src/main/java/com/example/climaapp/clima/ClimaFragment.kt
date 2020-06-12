package com.example.climaapp.clima

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.climaapp.R
import com.example.climaapp.databinding.FragmentWeatherBinding

class ClimaFragment: Fragment(){

    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }
}