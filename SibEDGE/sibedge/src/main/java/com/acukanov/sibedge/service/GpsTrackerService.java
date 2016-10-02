package com.acukanov.sibedge.service;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.acukanov.sibedge.BuildConfig;
import com.acukanov.sibedge.SibEdgeApplication;
import com.acukanov.sibedge.events.CoordinatesFetched;
import com.acukanov.sibedge.utils.GpsUtils;
import com.acukanov.sibedge.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

@Singleton
public class GpsTrackerService extends Service implements android.location.LocationListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(GpsTrackerService.class);
    private Handler mHandler;
    private static Runnable sRunnable;
    private LocationManager mLocationManager;
    private Location mLocation;
    private double mLatitude;
    private double mLongitude;
    private double mZeroValue = 0.0;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, GpsTrackerService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SibEdgeApplication.get(this).getApplicationComponent().inject(this);
        LogUtils.debug(LOG_TAG, "Service onCreate");
        mHandler = new Handler();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        sRunnable = new Runnable() {
            @Override
            public void run() {
                if (GpsUtils.isGpsEnabled(getApplicationContext()) &&
                        (GpsUtils.getLastKnownLocationGpsProvider(getApplicationContext()) != null ||
                                GpsUtils.getLastKnownLocationInternetProvider(getApplicationContext()) != null)) {
                    if (Double.compare(mLatitude, mZeroValue) == 0 && Double.compare(mLongitude, mZeroValue) == 0) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                    EventBus.getDefault().post(new CoordinatesFetched(mLatitude, mLongitude));
                    LogUtils.debug(LOG_TAG, "LOG!! " + mLatitude + " " + mLongitude);
                }
                mHandler.postDelayed(sRunnable, 10000);
            }
        };
        mHandler.post(sRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(sRunnable);
        LogUtils.debug(LOG_TAG, "Service stopped");
    }

    public static boolean isServiceRunning(Context context, Class aClass) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
