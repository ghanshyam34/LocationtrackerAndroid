package com.gs.locationtrackerdemo;
import androidx.core.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;

import com.ghanshyam.locationtracklib.Data;
import com.ghanshyam.locationtracklib.LocationConnection;
import com.ghanshyam.locationtracklib.LocationData;

/**
 * Created by Ghanshyam
 */
public class MainActivityJava extends BaseActivity{
    private LocationConnection locationConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkRequiredPermission(Constant.LOCATION_PERMISSION);
    }

    @Override
    protected void invokedWhenNoOrAllreadyPermissionGranted() {
        super.invokedWhenNoOrAllreadyPermissionGranted();
        connectService();
    }

    @Override
    protected void invokedWhenDeniedWithResult(int[] grantResults) {
        super.invokedWhenDeniedWithResult(grantResults);
    }

    @Override
    protected void invokedWhenPermissionGranted() {
        super.invokedWhenPermissionGranted();
        connectService();
    }

    @Override
    protected void onDestroy() {
        if(serviceConnection != null){
            serviceConnection.stopConnection();
        }
        super.onDestroy();
    }

    LocationConnection serviceConnection;
    public void connectService() {
        serviceConnection = new LocationConnection(getApplicationContext());
        Data data = new Data();
        data.setAlertOnce(true);
        data.setCategory(NotificationCompat.CATEGORY_SERVICE);
        data.setChannelId(Constant.CHANNEL_ID);
        data.setChannelName(Constant.CHANNEL_ID_NAME);
        data.setDISPLACEMENT(0);
        data.setFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS(1000);
        data.setUPDATE_INTERVAL_IN_MILLISECONDS(1000);
        data.setNotificationId(9083150);
        data.setNumber(0);
        data.setOnGoing(true);
        data.setSmallIcon(R.mipmap.ic_launcher);
        data.setVisibility(NotificationCompat.VISIBILITY_SECRET);
        serviceConnection.setData(data);
        //serviceConnection.isTrackingMode=true;
        serviceConnection.setAliveOnActivityFinished(true);
        serviceConnection.startLocationUpdates(new LocationConnection.LocationRequestListener() {
            @Override
            public void onLocationChanged(LocationData locationData) {
                Log.i(MainActivityJava.class.getSimpleName(), "Location = " + locationData.getLatitude() + "," + locationData.getLongitude() + "  Bearing = " + locationData.getBearing());
            }
        });
    }
}
