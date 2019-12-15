package com.gs.locationtrackerdemo;

import android.app.Application;

/**
 * Created by Ghanshyam.
 */
public class MainApplication extends Application {
    static MainApplication instance;
    public static synchronized MainApplication getInstance() {
        return instance;
    }

    private void init(Application app) {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

