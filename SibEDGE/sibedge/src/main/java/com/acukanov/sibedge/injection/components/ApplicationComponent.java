package com.acukanov.sibedge.injection.components;


import android.app.Application;
import android.content.Context;

import com.acukanov.sibedge.SibEdgeApplication;
import com.acukanov.sibedge.data.database.DatabaseHelper;
import com.acukanov.sibedge.injection.annotations.ApplicationContext;
import com.acukanov.sibedge.injection.modules.ApplicationModule;
import com.acukanov.sibedge.service.GpsStateReceiver;
import com.acukanov.sibedge.service.GpsTrackerService;
import com.acukanov.sibedge.service.NetworkStateReceiver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(SibEdgeApplication sibEdgeApplication);
    void inject(GpsStateReceiver gpsStateReceiver);
    void inject(GpsTrackerService gpsTrackerService);
    void inject(NetworkStateReceiver networkStateReceiver);

    @ApplicationContext
    Context context();
    Application application();
    DatabaseHelper databaseHelper();
}
