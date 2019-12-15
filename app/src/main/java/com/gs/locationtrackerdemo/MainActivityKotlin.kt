package com.gs.locationtrackerdemo

import android.os.Bundle
import android.util.Log

import androidx.core.app.NotificationCompat
import com.ghanshyam.locationtracklib.Data
import com.ghanshyam.locationtracklib.LocationConnection

/**
 * Created by Ghanshyam
 */
class MainActivityKotlin : BaseActivityKotlin() {
    private val locationConnection: LocationConnection? = null

    internal var serviceConnection: LocationConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkRequiredPermission(Constant.LOCATION_PERMISSION)
    }

    override fun invokedWhenNoOrAllreadyPermissionGranted() {
        super.invokedWhenNoOrAllreadyPermissionGranted()
        connectService()
    }

    override fun invokedWhenDeniedWithResult(grantResults: IntArray) {
        super.invokedWhenDeniedWithResult(grantResults)
    }

    override fun invokedWhenPermissionGranted() {
        super.invokedWhenPermissionGranted()
        connectService()
    }

    override fun onDestroy() {
        if (serviceConnection != null) {
            serviceConnection!!.stopConnection()
        }
        super.onDestroy()
    }

    fun connectService() {
        serviceConnection = LocationConnection(applicationContext)
        val data = Data()
        data.isAlertOnce = true
        data.category = NotificationCompat.CATEGORY_SERVICE
        data.channelId = Constant.CHANNEL_ID
        data.channelName = Constant.CHANNEL_ID_NAME
        data.displacement = 0
        data.fastesT_UPDATE_INTERVAL_IN_MILLISECONDS = 1000
        data.updatE_INTERVAL_IN_MILLISECONDS = 1000
        data.notificationId = 9083150
        data.number = 0
        data.isOnGoing = true
        data.smallIcon = R.mipmap.ic_launcher
        data.visibility = NotificationCompat.VISIBILITY_SECRET
        serviceConnection!!.setData(data)
        //serviceConnection.isTrackingMode=true;
        serviceConnection!!.isAliveOnActivityFinished = true
        serviceConnection!!.startLocationUpdates { locationData ->
            Log.i(MainActivityKotlin::class.java.simpleName, "Location = " + locationData.latitude + "," + locationData.longitude + "  Bearing = " + locationData.bearing)
        }
    }
}
