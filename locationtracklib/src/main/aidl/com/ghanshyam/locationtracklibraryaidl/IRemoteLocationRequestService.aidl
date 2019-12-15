// IRemoteLocationRequestService.aidl
package com.ghanshyam.locationtracklibraryaidl;
// Declare any non-default types here with import statements

interface IRemoteLocationRequestService {
   void updateLattitudeLongitude(double latitude, double longitude, float bearing);
   void requestLocationUpdates(IMyAidlInterface listener);
   void updateData(in Bundle bundle);
   void stopUpdates();
}
interface IMyAidlInterface {
   void updateLocation(in Bundle bundle);
   void stop();
}
