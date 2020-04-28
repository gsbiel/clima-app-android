package com.example.climaapp;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.climaapp.interfaces.WeatherBrainDelegate;
import com.example.climaapp.services.WeatherAPIClient;
import org.json.JSONObject;

public class WeatherBrain {

    private static String APP_ID = "b637e2f34ebe3de4456fe8f6a71e83cf";
    private static String URL_BASE = "https://api.openweathermap.org/data/2.5/weather?appid=" + WeatherBrain.APP_ID + "&units=metric";

    public WeatherBrainDelegate delegate;
    Context context;

    WeatherBrain(Context context) {
        this.context = context;
    }

    public void fetchWeatherDataForCity(String city) {
        String url = WeatherBrain.URL_BASE + "&q=" + city;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                delegate.didFinishFetchingDataFromAPI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                delegate.didFinishFetchingDataFromAPIWithError(error);
            }
        });
        WeatherAPIClient.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void fetchWeatherDataForLocation(double latitude, double longitude){
        System.out.println("Localização: " + latitude + ", " + longitude);
        String url = WeatherBrain.URL_BASE + "&lat=" + latitude + "&lon=" + longitude;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                delegate.didFinishFetchingDataFromAPI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                delegate.didFinishFetchingDataFromAPIWithError(error);
            }
        });
        WeatherAPIClient.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
