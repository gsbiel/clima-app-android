package com.example.climaapp.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class WeatherAPIClient {

    private static WeatherAPIClient clientInstance;
    private static Context context;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private WeatherAPIClient(Context context){
        this.context = context;
        this.requestQueue = this.getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });

    }

    public static synchronized WeatherAPIClient getInstance(Context context) {
        if (WeatherAPIClient.clientInstance == null) {
            WeatherAPIClient.clientInstance = new WeatherAPIClient(context);
        }
        return WeatherAPIClient.clientInstance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            this.requestQueue = Volley.newRequestQueue(this.context.getApplicationContext());
        }
        return this.requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        this.getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }

}
