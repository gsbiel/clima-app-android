package com.example.climaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
        setContentView(R.layout.activity_main)
    }

    private fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar!!.hide()
    }

}