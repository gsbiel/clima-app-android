package com.example.climaapp.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface WeatherBrainDelegate {
    public void didFinishFetchingDataFromAPI(JSONObject weatherData);
    public void didFinishFetchingDataFromAPIWithError(VolleyError error);
}
