    //package com.example.kent.hyperdeals.FragmentActivities
    //
    //import android.Manifest
    //import android.annotation.SuppressLint
    //import android.content.BroadcastReceiver
    //import android.content.Context
    //import android.content.Intent
    //import android.content.IntentFilter
    //import android.content.pm.PackageManager
    //import android.net.wifi.WifiManager
    //import android.net.wifi.rtt.RangingRequest
    //import android.net.wifi.rtt.RangingResult
    //import android.net.wifi.rtt.RangingResultCallback
    //import android.net.wifi.rtt.WifiRttManager
    //import android.os.Build
    //
    //import android.os.Bundle
    //import android.support.annotation.RequiresApi
    //import android.support.v4.app.ActivityCompat
    //import android.support.v7.app.AppCompatActivity
    //import android.view.View
    //import com.example.kent.hyperdeals.R
    //import kotlinx.android.synthetic.main.activity_main.*
    //
    //@RequiresApi(Build.VERSION_CODES.M)
    //class MainActivity : AppCompatActivity() {
    //
    //    fun <T> MutableList<T>.takeMax(max: Int) = this.subList(0, minOf(size, max))
    //
    //    companion object {
    //        const val REQUEST_CODE_ACCESS_COARSE_LOCATION = 1
    //    }
    //
    //    private val mWifiManager by lazy { applicationContext.getSystemService(WifiManager::class.java) as WifiManager }
    //
    //    @SuppressLint("NewApi")
    //    override fun onCreate(savedInstanceState: Bundle?) {
    //        super.onCreate(savedInstanceState)
    //        setContentView(R.layout.activity_rtt)
    //
    //        val rttManager = getSystemService(WifiRttManager::class.java) as WifiRttManager
    //
    //            mWifiManager.startScan()
    //
    //
    //        // REQUEST PERMISSION
    //        when {
    //            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED -> // permission is not granted
    //                ActivityCompat.requestPermissions(this,
    //                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_ACCESS_COARSE_LOCATION)
    //
    //        }
    //
    //        // REGISTER BROADCAST RECEIVER
    //        registerReceiver(object : BroadcastReceiver() {
    //            @SuppressLint("MissingPermission")
    //            @RequiresApi(Build.VERSION_CODES.P)
    //            override fun onReceive(context: Context, intent: Intent) {
    //                with(mWifiManager.scanResults) {
    //                    if (size > 0) {
    //                        val rangingRequest = RangingRequest.Builder()
    //                                .addAccessPoints(takeMax(RangingRequest.getMaxPeers()))
    //                                .build()
    //
    //                        rttManager.startRanging(rangingRequest, object : RangingResultCallback() {
    //                            override fun onRangingResults(results: MutableList<RangingResult>) {
    //                                results
    //                                        .filter { it.status == RangingResult.STATUS_SUCCESS }
    //                                        .forEach {
    //                                        }
    //                            }
    //
    //                            override fun onRangingFailure(p0: Int) {
    //
    //                            }
    //                        }, null)
    //                    }
    //                }
    //            }
    //
    //        }, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    //    }
    //
    //    override fun onRequestPermissionsResult(requestCode: Int,
    //                                            permissions: Array<String>, grantResults: IntArray) {
    //        when (requestCode) {
    //            REQUEST_CODE_ACCESS_COARSE_LOCATION -> {
    //                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
    //                    // permission was granted
    //                } else {
    //                    // permission denied, boo!
    //                }
    //                return
    //            }
    //            else -> {
    //                // Ignore all other requests.
    //            }
    //        }
    //    }
    //
    //
    //}
    //
    //private fun WifiRttManager.startRanging(rangingRequest: RangingRequest?, rangingResultCallback: RangingResultCallback, nothing: Nothing?) {
    //
    //}
