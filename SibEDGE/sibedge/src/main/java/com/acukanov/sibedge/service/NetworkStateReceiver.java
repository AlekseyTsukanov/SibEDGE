package com.acukanov.sibedge.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.acukanov.sibedge.events.NetworkConnected;
import com.acukanov.sibedge.events.NetworkDisconnected;

import org.greenrobot.eventbus.EventBus;

public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            boolean isConnected = activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                EventBus.getDefault().post(new NetworkConnected());
            } else {
                EventBus.getDefault().post(new NetworkDisconnected());
            }
        } else {
            EventBus.getDefault().post(new NetworkDisconnected());
        }
    }
}
