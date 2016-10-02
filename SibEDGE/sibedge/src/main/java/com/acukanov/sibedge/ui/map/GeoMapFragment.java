package com.acukanov.sibedge.ui.map;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.events.CoordinatesFetched;
import com.acukanov.sibedge.events.GpsStateChanged;
import com.acukanov.sibedge.service.GpsTrackerService;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.utils.DialogFactory;
import com.acukanov.sibedge.utils.GpsUtils;
import com.acukanov.sibedge.utils.LogUtils;
import com.acukanov.sibedge.utils.PermissionsUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GeoMapFragment extends BaseFragment implements IMapFragmentView, OnMapReadyCallback {
    private static final String LOG_TAG = LogUtils.makeLogTag(GeoMapFragment.class);
    private static final int REQUEST_PERMISSION_LOCATION = 0;
    private Activity mActivity;
    @Inject MapFragmentPresenter mMapPresenter;
    @InjectView(R.id.map_view) MapView mMapView;
    private GoogleMap mGoogleMap;
    private Intent mGpsTrackerIntent;

    public GeoMapFragment() {}

    public static GeoMapFragment newInstance() {
        return new GeoMapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).activityComponent().inject(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.inject(this, rootView);
        mMapPresenter.attachView(this);
        mMapView.onCreate(savedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (!PermissionsUtils.hasPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionsUtils.requestPermissionsSafely(
                    mActivity,
                    this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSION_LOCATION
            );
        } else {
            mMapPresenter.initializeMap();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (GpsTrackerService.isServiceRunning(mActivity, GpsTrackerService.class)) {
            mActivity.stopService(mGpsTrackerIntent);
        }
        EventBus.getDefault().unregister(this);
        mMapPresenter.detachView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGpsTrackerIntent = GpsTrackerService.getStartIntent(mActivity);
        if (GpsTrackerService.isServiceRunning(mActivity, GpsTrackerService.class)) {
            mActivity.stopService(mGpsTrackerIntent);
        }
        mActivity.startService(mGpsTrackerIntent);
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapPresenter.initializeMap();
                } else {
                    DialogFactory.createSimpleOkErrorDialog(mActivity,
                            R.string.dialog_title_location_permission,
                            R.string.dialog_message_location_premission).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CoordinatesFetched event) {
        LatLng coordinates = new LatLng(event.getLat(), event.getLon());
        if (mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));
            mGoogleMap.clear();
            mGoogleMap.addMarker(new MarkerOptions()
                    .title("Current position")
                    .position(coordinates));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GpsStateChanged event) {
        if (!GpsUtils.isGpsEnabled(mActivity)) {
            DialogFactory.createSimpleOkErrorDialog(
                    mActivity,
                    R.string.dialog_title_gps_disabled,
                    R.string.dialog_message_gps).show();
        } else {
            if (!GpsTrackerService.isServiceRunning(mActivity, GpsTrackerService.class)) {
                mActivity.startService(mGpsTrackerIntent);
            }
        }
    }

    @Override
    public void onMapInitialize() {
        mMapView.getMapAsync(this);
    }
}
