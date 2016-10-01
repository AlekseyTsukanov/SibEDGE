package com.acukanov.sibedge.utils;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.acukanov.sibedge.R;

public class ActivityCommon {

    public static Toolbar setUpActionBarToolbar(AppCompatActivity activity) {
        Toolbar actionBarToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (actionBarToolbar != null) {
            activity.setSupportActionBar(actionBarToolbar);
        }
        return actionBarToolbar;
    }

    public static ActionBar setHomeAsUp(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return actionBar;
    }
}
