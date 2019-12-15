package com.ghanshyam.locationtracklib;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ghanshyam.locationtracklibraryaidl.IMyAidlInterface;
import com.ghanshyam.locationtracklibraryaidl.IRemoteLocationRequestService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
/**
 * Created by Ghanshyam.
 */
public class LocationTracker extends Service {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private String TAG = LocationTracker.class.getSimpleName();

    static LocationTracker instance;

    public static LocationTracker getInstance() {
        if (instance == null) {
            instance = new LocationTracker();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setData(LocalStorage.getInstance().getData());
        try {
            if (Build.VERSION.SDK_INT >= 26 && channelId != null && !"".equalsIgnoreCase(channelId)) {
                NotificationChannel channel = new NotificationChannel(channelId, channelName,
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(null, null);
                channel.setShowBadge(false);
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.deleteNotificationChannel(channelId);
                notificationManager.createNotificationChannel(channel);

                Notification notification = createNotification(getApplicationContext(), channelId, 0);
                if (notification == null) {
                    notification = new NotificationCompat.Builder(this, channelId).build();
                }
                startForeground(notificationId, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Notification createNotification(Context context, String channelid, int type) {
        try {
            Intent mainIntent = new Intent(context, LocationTracker.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, mainIntent, 0);
            return new NotificationCompat.Builder(context, channelid)
                    .setSmallIcon(smallIcon)
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setOnlyAlertOnce(isAlertOnce)
                    .setOngoing(isOnGoing)
                    .setPriority(priority)
                    .setCategory(category)
                    .setVisibility(visibility)
                    .setNumber(number)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String channelId;
    String channelName;
    int notificationId;
    int smallIcon = android.R.drawable.ic_menu_mylocation;
    NotificationCompat.Style style;
    boolean isAlertOnce = true;
    boolean isOnGoing = true;
    int priority = NotificationCompat.PRIORITY_HIGH;
    String category = NotificationCompat.CATEGORY_SERVICE;
    int visibility = Notification.VISIBILITY_PRIVATE;
    int number;
    private long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private int DISPLACEMENT = 0;

    public void setData(Data data) {
        LocationTracker.this.channelId = data.getChannelId();
        LocationTracker.this.channelName = data.getChannelName();
        LocationTracker.this.smallIcon = data.getSmallIcon();
        LocationTracker.this.isAlertOnce = data.isAlertOnce();
        LocationTracker.this.isOnGoing = data.isOnGoing();
        LocationTracker.this.priority = data.getPriority();
        LocationTracker.this.category = data.getCategory();
        LocationTracker.this.visibility = data.getVisibility();
        LocationTracker.this.number = data.getNumber();
        LocationTracker.this.notificationId = data.getNotificationId();
        LocationTracker.this.UPDATE_INTERVAL_IN_MILLISECONDS = data.getUPDATE_INTERVAL_IN_MILLISECONDS();
        LocationTracker.this.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = data.getFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS();
        LocationTracker.this.DISPLACEMENT = data.getDISPLACEMENT();
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateLattitudeLongitude(location.getLatitude(), location.getLongitude(), location.getBearing());
                }
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    private void init(final Context ctx) {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
            locationRequest = new LocationRequest();
            locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setSmallestDisplacement(DISPLACEMENT);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationServices.getSettingsClient(ctx).checkLocationSettings(builder.build()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            requestingLocationUpdates(ctx);
                            break;
                    }
                }
             }).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            requestingLocationUpdates(ctx);
                        }
                    });

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            init(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public void requestingLocationUpdates(final Context ctx) {
        try {
            try {
                if (fusedLocationProviderClient != null) {
                    fusedLocationProviderClient
                            .removeLocationUpdates(locationCallback);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback, Looper.myLooper());

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFusedLocationUpdate() {
        try {
            if (fusedLocationProviderClient != null) {
                fusedLocationProviderClient
                        .removeLocationUpdates(locationCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface RequestLocationListener {
        public void onLocationUpdate(LocationData locationData);
    }

    static RequestLocationListener requestLocationListener;
    public void requestLocationUpdates(RequestLocationListener listener) {
        requestLocationListener = listener;
    }

    public void requestLocationUpdatesInit(Context ctx,RequestLocationListener listener){
        requestLocationListener = listener;
        init(ctx);
    }

    public static LocationData getLastLocation(Context ctx) {
        LocationData locationData = new LocationData();
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        } else {
            FusedLocationProviderClient mFusedLocationClient = null;
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
            if (mFusedLocationClient != null) {
                Task<Location> task = mFusedLocationClient.getLastLocation();
                if (task != null) {
                    Location location1 = task.getResult();
                    if (location1 != null) {
                        double latitude = location1.getLatitude();
                        double longitude = location1.getLongitude();
                        float bearing = location1.getBearing();
                        locationData.setLatitude(latitude);
                        locationData.setLongitude(longitude);
                        locationData.setBearing(bearing);
                    }
                }
            }
        }
        return locationData;
    }

    public void updateLattitudeLongitude(double latitude, double longitude, float bearing) {
//        Log.i(TAG, "Location = " + latitude + "," + longitude + "  Bearing = " + bearing);
        try {
            if (mBinder != null) {
                mBinder.updateLattitudeLongitude(latitude, longitude, bearing);
            }
            if(requestLocationListener != null){
                LocationData locationData = new LocationData();
                locationData.setLatitude(latitude);
                locationData.setLongitude(longitude);
                locationData.setBearing(bearing);
                requestLocationListener.onLocationUpdate(locationData);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopLocationService(Context context, boolean isForceStopped) {
        Log.i(TAG, "stopLocationService == " + isForceStopped);
        try {
            stopForeground(true);
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startLocationService(Context context) {
        try {
            Intent intent = new Intent(context, LocationTracker.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        try {
            init(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private final IRemoteLocationRequestService.Stub mBinder = new IRemoteLocationRequestService.Stub() {
        IMyAidlInterface iremote;
        public Bundle bundle = new Bundle();

        @Override
        public void updateLattitudeLongitude(double latitude, double longitude, float bearing) throws RemoteException {
            if (iremote != null) {
                LocationData locationData = new LocationData();
                locationData.setLatitude(latitude);
                locationData.setLongitude(longitude);
                locationData.setBearing(bearing);
                bundle.clear();
                bundle.putParcelable("locationData", locationData);
                iremote.updateLocation(bundle);
            }
        }

        @Override
        public void requestLocationUpdates(IMyAidlInterface listener) throws RemoteException {
            this.iremote = listener;
        }

        @Override
        public void updateData(Bundle bundle) throws RemoteException {
            Data data = bundle.getParcelable("data_location");
            setData(data);
        }

        @Override
        public void stopUpdates() throws RemoteException {
            if (iremote != null) {
                iremote.stop();
            }
        }
    };
}
