package com.gs.locationtrackerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.ghanshyam.locationtracklib.Data;
import com.ghanshyam.locationtracklib.LocalStorage;
import com.ghanshyam.locationtracklib.LocationData;
import com.ghanshyam.locationtracklib.LocationTracker;
import com.ghanshyam.locationtracklibraryaidl.IMyAidlInterface;
import com.ghanshyam.locationtracklibraryaidl.IRemoteLocationRequestService;

/**
 * Created by Ghanshyam.
 */
public class LocationSetup {
    private IRemoteLocationRequestService service;
    Context context;
    public LocationSetup(Context context) {
        this.context = context;
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IRemoteLocationRequestService.Stub.asInterface((IBinder) boundService);
            LocationTracker.startLocationService(context);
            updateLocationRequest();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    LocationSetup.LocationRequestListener requestListener;
    public void startLocationUpdates(LocationSetup.LocationRequestListener requestListener){
        this.requestListener = requestListener;
        startConnection();
    }

    private void startConnection() {
        Intent intent = new Intent(context, LocationTracker.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopConnection() {
        context.unbindService(serviceConnection);
        LocalStorage.getInstance().clearData();
    }

    public void setData(Data data){
        LocalStorage.getInstance().createPreference(context,"myPreference");
        LocalStorage.getInstance().saveData(data);
    }

    private void updateLocationRequest(){
        try {
            service.requestLocationUpdates(new IMyAidlInterface.Stub() {
                @Override
                public void updateLocation(Bundle bundle) throws RemoteException {
                    LocationData locationData = (LocationData) bundle.getParcelable("locationData");
                    if(requestListener != null){
                        requestListener.onLocationChanged(locationData);
                    }
                }

                @Override
                public void stop() throws RemoteException {
                    stopConnection();
                }
            });
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public interface LocationRequestListener{
        void onLocationChanged(LocationData locationData);
    }
}
