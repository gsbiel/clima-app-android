package com.example.climaapp.clima

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.climaapp.R
import com.example.climaapp.databinding.FragmentWeatherBinding

class ClimaFragment: Fragment(), TextView.OnEditorActionListener{

    private lateinit var viewModel: ClimaViewModel
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProviders.of(this).get(ClimaViewModel::class.java)
        binding.viewModel = viewModel

        binding.searchEditText.setOnEditorActionListener(this)

        return binding.root
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(v?.id == binding.searchEditText.id && actionId == EditorInfo.IME_ACTION_SEND){
            viewModel.onSendButtonPressed()
            return false
        }
        return false
    }

}