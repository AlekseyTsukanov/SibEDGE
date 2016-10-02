package com.acukanov.sibedge.ui.scaling.browse;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.utils.ActivityCommon;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScalingActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_URI = "extra_image_uri";
    @InjectView(R.id.toolbar) Toolbar mToolbar;

    public static void startActivity(Activity activity, Fragment fragment, Uri data) {
        Intent intent = new Intent(activity, ScalingActivity.class);
        intent.putExtra(EXTRA_IMAGE_URI, data.toString());
        if (fragment != null) {
            fragment.startActivity(intent);
        } else {
            activity.startActivity(intent);
        }
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaling);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        ActivityCommon.setHomeAsUp(this);
        mToolbar.setNavigationOnClickListener(l -> {
            onBackPressed();
        });
        String extraArg = getIntent().getStringExtra(EXTRA_IMAGE_URI);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, ScalingFragment.newInstance(extraArg)).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
