package com.ghanshyam.locationtracklib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.ghanshyam.locationtracklibraryaidl.IMyAidlInterface;
import com.ghanshyam.locationtracklibraryaidl.IRemoteLocationRequestService;
/**
 * Created by Ghanshyam.
 */
public class LocationConnection implements ServiceConnection {
    private IRemoteLocationRequestService service;
    Context context;
    public boolean isTrackingMode;
    boolean isAliveOnActivityFinished = true;
    public LocationConnection(Context context) {
        this.context = context;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder boundService) {
        service = IRemoteLocationRequestService.Stub.asInterface((IBinder) boundService);
        if(isAliveOnActivityFinished || isTrackingMode) {
            LocationTracker.startLocationService(context);
        }

        updateLocationRequest();
    }

    LocationRequestListener requestListener;
    public void startLocationUpdates(LocationRequestListener requestListener){
        this.requestListener = requestListener;
        if(!isAliveOnActivityFinished) {
            startConnection();
        }else{
            LocationTracker.startLocationService(context);
            updateLocationRequest();
        }
    }

    private void startConnection() {
        Intent intent = new Intent(context, LocationTracker.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void stopConnection() {
        if(!isAliveOnActivityFinished) {
            context.unbindService(this);
            LocalStorage.getInstance().clearData();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    public void setData(Data data){
        LocalStorage.getInstance().createPreference(context,"myPreference");
        LocalStorage.getInstance().saveData(data);
    }

    private void updateLocationRequest(){
        try {
            if(isAliveOnActivityFinished) {
                LocationTracker.getInstance().requestLocationUpdates(new LocationTracker.RequestLocationListener() {
                    @Override
                    public void onLocationUpdate(LocationData locationData) {
                        if(requestListener != null){
                            requestListener.onLocationChanged(locationData);
                        }
                    }
                });

            }else{
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
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public boolean isAliveOnActivityFinished() {
        return isAliveOnActivityFinished;
    }

    public void setAliveOnActivityFinished(boolean aliveOnActivityFinished) {
        isAliveOnActivityFinished = aliveOnActivityFinished;
    }

    public interface LocationRequestListener{
        void onLocationChanged(LocationData locationData);
    }
}
