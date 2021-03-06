package com.example.climaapp.clima

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.climaapp.R
import com.example.climaapp.database.WeatherDatabase
import com.example.climaapp.databinding.FragmentWeatherBinding
import com.example.climaapp.hideKeyboard

private const val PERMISSION_REQUEST = 10

class ClimaFragment: Fragment(), TextView.OnEditorActionListener{

    private lateinit var viewModel: ClimaViewModel
    private lateinit var viewModelFactory: ClimaViewModelFactory
    private lateinit var binding: FragmentWeatherBinding

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val databaseDao = WeatherDatabase.getInstance(application).weatherDatabaseDao
        viewModelFactory = ClimaViewModelFactory(databaseDao, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ClimaViewModel::class.java)
        binding.viewModel = viewModel

        binding.searchEditText.setOnEditorActionListener(this)

        registerForViewModelEvents()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                Log.i("ClimaFragment", "Todas as permissões okay!")
                if(viewModel.shouldGetWeatherDataBasedInCurrentLocation.value!!){
                    viewModel.shouldGetWeatherDataBasedInCurrentLocation.value = false
                    getLocation()
                }
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            Log.i("ClimaFragment", "Versão do android não compatível")
        }

        return binding.root
    }

    private fun registerForViewModelEvents() {

        viewModel.refreshEvent.observe(this, Observer {
            if(it){
                Log.i("ClimaViewModel", "Refresh Button pressed!")
                if(!viewModel.permissionsGranted.value!!){
                    Toast.makeText(context!!, "Permissions has been denied. Click on Set Permission button to allow acces to location services.", Toast.LENGTH_SHORT).show()
                }
                viewModel.refreshEventCompleted()
            }
        })

        viewModel.setPermissionEvent.observe(this, Observer{
            if(it){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(permissions, PERMISSION_REQUEST)
                } else {
                    Log.i("ClimaFragment", "Versão do android não compatível")
                }
                viewModel.setPermissionEventCompleted()
            }
        })
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(v?.id == binding.searchEditText.id && actionId == EditorInfo.IME_ACTION_SEND){
            viewModel.setCityNameTo(v.text.toString())
            viewModel.onSendButtonPressed()
            binding.searchEditText.clearFocus()
            dismissSoftKeyboard()
            return true
        }
        return false
    }

    private fun dismissSoftKeyboard(){
        this.hideKeyboard()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
//                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
//                            Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
//                            Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }

            if (hasNetwork) {
//                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
//                            Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
//                            Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
//                    Log.d("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
//                    Log.d("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                    viewModel.setLatitudeTo(locationNetwork!!.latitude)
                    viewModel.setLongitudeTo(locationNetwork!!.longitude)
                    viewModel.fetchWeatherDataBasedOnLatLongEntry()
                }else{
//                    Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
//                    Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                    viewModel.setLatitudeTo(locationGps!!.latitude)
                    viewModel.setLongitudeTo(locationGps!!.longitude)
                    viewModel.fetchWeatherDataBasedOnLatLongEntry()
                }
            }

        } else {
            Log.i("ClimaFragment", "Não há nem internet nem GPS ativado")
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(context!!,permissionArray[i]) == PermissionChecker.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
//                        Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
                        viewModel.setPermissionsTo(false)
                    } else {
                        Toast.makeText(context!!, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess) {
                Log.i("ClimaFragment", "allSuccess!")
                getLocation()
                viewModel.setPermissionsTo(true)
            }
        }
    }

}