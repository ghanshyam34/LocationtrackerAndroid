package com.gs.locationtrackerdemo;

import android.Manifest;

/**
 * Created by Ghanshyam.
 */
public class Constant {

  public static final String CHANNEL_ID = "LocationTracker_ID";
  public static final String CHANNEL_ID_NAME = "LocationTracker_NAME";

  public static final String[] LOCATION_PERMISSION = {
          Manifest.permission.ACCESS_BACKGROUND_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION,
  };
}
