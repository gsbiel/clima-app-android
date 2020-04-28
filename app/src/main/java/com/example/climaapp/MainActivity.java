package com.example.climaapp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.climaapp.interfaces.EditTextWatcherDelegate;
import com.example.climaapp.interfaces.TimeOutDelegate;
import com.example.climaapp.interfaces.WeatherBrainDelegate;
import com.example.climaapp.watchers.EditTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
                                                                View.OnFocusChangeListener,
                                                                View.OnClickListener,
                                                                TimeOutDelegate,
                                                                EditTextWatcherDelegate,
                                                                WeatherBrainDelegate
{

    private static final int PERMISSION_REQUEST_CODE = 200;

    ConstraintLayout mainScreen;
    ImageView refreshBtn;
    ImageView searchBtn;
    EditText textField;
    TextView eraseTextFieldBtn;
    EditTextWatcher textWatcher;

    ImageView weatherImage;
    TextView localNameLabel;
    TextView temperatureLabel;

    WeatherBrain weatherBrain;

    String currentCity;

    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            weatherBrain.fetchWeatherDataForLocation(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Status Changed", String.valueOf(status));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Provider Enabled", provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Provider Disabled", provider);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hideActionBar();
        setContentView(R.layout.activity_main);
        this.weatherBrain = new WeatherBrain(this.getApplicationContext());
        weatherBrain.delegate = this;
        this.catchReferencesFromLayout();
        this.registerForUIEvents();
        this.hideEraseButton(true);
        this.textWatcher.delegate = this;
        this.fetchWeatherDataForCurrentLocation();
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void catchReferencesFromLayout(){
        this.mainScreen = findViewById(R.id.mainScreen);
        this.refreshBtn = findViewById(R.id.refreshBtn);
        this.searchBtn = findViewById(R.id.searchBtn);
        this.textField = findViewById(R.id.searchTextField);
        this.eraseTextFieldBtn = findViewById(R.id.eraseTextBtn);
        this.textWatcher = new EditTextWatcher(this.textField, 1);
    }

    private void registerForUIEvents(){
        this.mainScreen.setOnClickListener(this);
        this.textField.setOnFocusChangeListener(this);
        this.refreshBtn.setOnClickListener(this);
        this.searchBtn.setOnClickListener(this);
        this.eraseTextFieldBtn.setOnClickListener(this);
        this.textField.addTextChangedListener(this.textWatcher);
    }

    public void fetchWeatherDataForCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE );
            return;
        }

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final boolean gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            this.askForGPS();
        }else{
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(true);
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

            lm.requestSingleUpdate(criteria, locationListener, null);   //looper = null
        }

    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void askForGPS(){
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Acesso ao GPS requerido. Pressione Ok para ativá-lo.")
                .setTitle("GPS")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enableLocationSettings();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId() == R.id.searchTextField){
            if(hasFocus){
                System.out.println("Search Text Field has focus!");
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,255)));
            }else{
                System.out.println("Search Text Field has not focus anymore!");
                v.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0,0,0)));
            }
        }
    }

    @Override
    public void onClick(View v) {
        this.hideSoftKeyBoard();
        if(v.getId() == R.id.searchBtn){
            this.setAlphaForView(v);
            this.currentCity = this.textField.getText().toString();
            weatherBrain.fetchWeatherDataForCity(this.currentCity);
        }else if(v.getId() == R.id.refreshBtn){
            this.setAlphaForView(v);
            if(this.currentCity != "" && this.currentCity != null){
                weatherBrain.fetchWeatherDataForCity(this.currentCity);
            }
            else{
                this.fetchWeatherDataForCurrentLocation();
            }
        }else if(v.getId() == R.id.mainScreen){
            this.hideSoftKeyBoard();
        }else if(v.getId() == R.id.eraseTextBtn){
            this.setAlphaForView(v);
            this.textField.setText("");
        }
    }

    private void setAlphaForView(View v){
        v.setAlpha((float) 0.2);
        TimeOut timer = new TimeOut(200,this);
        int viewId = v.getId();
        timer.execute(viewId);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void hideEraseButton(Boolean flag){
        if(flag){
            this.eraseTextFieldBtn.setVisibility(View.INVISIBLE);
        }else{
            this.eraseTextFieldBtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(String localName, int weatherType, double temperature){

        this.localNameLabel.setText(localName);
        this.temperatureLabel.setText(Double.toString(temperature));

        if(weatherType>=200 && weatherType <= 232){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_bolt);
        }else if(weatherType>=300 && weatherType <= 321){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_drizzle);
        }else if(weatherType>=500 && weatherType <= 531){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_rain);
        }else if(weatherType>=600 && weatherType <= 622){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_snow;
        }else if(weatherType>=701 && weatherType <= 781){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_fog);
        }else if(weatherType==800){
            this.weatherImage.setImageResource(R.drawable.ic_sun);
        }else if(weatherType>=801 && weatherType <=804){
            this.weatherImage.setImageResource(R.drawable.ic_cloud_bolt;
        }else{
            this.weatherImage.setImageResource(R.drawable.ic_cloud);
        }

    }

    @Override
    public void didFinishTimer(Integer viewId) {
        View imageButton = findViewById(viewId);
        imageButton.setAlpha((float) 1.0);
    }

    @Override
    public void eraseButtonShouldAppear(Boolean flag) {
        this.hideEraseButton(!flag);
    }

    @Override
    public void didFinishFetchingDataFromAPI(JSONObject weatherData) {
        System.out.println("Dados buscados!");

        try {
            String localName = weatherData.getString("name");
            int weatherType = weatherData.getJSONArray("weather").getJSONObject(0).getInt("int");
            double temperature = weatherData.getJSONObject("main").getDouble("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void didFinishFetchingDataFromAPIWithError(VolleyError error) {
        System.out.println("Erro na requisição: " + error.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchWeatherDataForCurrentLocation();
                }
                return;
            }
        }
    }
}
