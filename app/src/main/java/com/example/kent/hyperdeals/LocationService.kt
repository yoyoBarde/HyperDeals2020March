package com.example.kent.hyperdeals

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.kent.hyperdeals.FragmentActivities.FragmentCategory
import com.example.kent.hyperdeals.Home.HomeAdapter
import com.google.android.gms.internal.zzahn
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragmentcategory.*
import java.lang.Exception

class LocationService : Service() {

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    val TAG="LocationService"
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
    }




    fun getLocation() {

        Log.e(TAG, "ASDADASDAJDISAjDAJSIOSJDIASJDOAJDIAJGAGO kaba")
        Log.e(TAG, "yoyobarde")

        locationManager = this!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.e(TAG, location.toString())
                Log.e(TAG, "Longtitude Service - " + location.latitude + "  Latitude - " + location.longitude)
                Log.e(TAG, "Longtitude Service - " + location.latitude + "  Latitude - " + location.longitude)
                Log.e(TAG, "Longtitude Service - " + location.latitude + "  Latitude - " + location.longitude)



            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(
                            this!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this!!.applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

            // locationManager.!!requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f,);

        }
    }

}
