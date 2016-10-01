package com.acukanov.sibedge;


import android.app.Application;
import android.content.Context;

import com.acukanov.sibedge.data.database.DatabaseHelper;
import com.acukanov.sibedge.injection.components.ApplicationComponent;
import com.acukanov.sibedge.injection.components.DaggerApplicationComponent;
import com.acukanov.sibedge.injection.modules.ApplicationModule;

import javax.inject.Inject;

public class SibEdgeApplication extends Application {
    @Inject DatabaseHelper databaseHelper;
    private ApplicationComponent mApplicationComponent;

    public static SibEdgeApplication get(Context context) {
        return (SibEdgeApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
