package com.acukanov.sibedge.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.acukanov.sibedge.SibEdgeApplication;
import com.acukanov.sibedge.injection.components.ActivityComponent;
import com.acukanov.sibedge.injection.components.DaggerActivityComponent;
import com.acukanov.sibedge.injection.modules.ActivityModule;

public class BaseActivity extends AppCompatActivity {
    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(SibEdgeApplication.get(this).getApplicationComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
