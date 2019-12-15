package com.ghanshyam.locationtracklib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ghanshyam.
 */
public class LocationData implements Parcelable {

    private double latitude;
    private double longitude;
    private float bearing;

    public LocationData() {

    }

    private LocationData(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        bearing = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeFloat(bearing);
    }

    public static final Parcelable.Creator<LocationData> CREATOR
            = new Parcelable.Creator<LocationData>() {
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }
}
