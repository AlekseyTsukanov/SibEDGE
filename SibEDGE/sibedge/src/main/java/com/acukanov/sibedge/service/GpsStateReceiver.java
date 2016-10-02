package com.acukanov.sibedge.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.acukanov.sibedge.SibEdgeApplication;
import com.acukanov.sibedge.events.GpsStateChanged;

import org.greenrobot.eventbus.EventBus;

public class GpsStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SibEdgeApplication.get(context).getApplicationComponent().inject(this);
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            EventBus.getDefault().post(new GpsStateChanged());
        }
    }
}
