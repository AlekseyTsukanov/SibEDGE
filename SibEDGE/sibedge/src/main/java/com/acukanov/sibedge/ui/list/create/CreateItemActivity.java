package com.acukanov.sibedge.ui.list.create;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.utils.ActivityCommon;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CreateItemActivity extends BaseActivity {
    @InjectView(R.id.toolbar) Toolbar mToolbar;

    public static void startActivityForResult(Activity activity, Fragment fragment, int requestCode) {
        Intent intent = new Intent(activity, CreateItemActivity.class);
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        ActivityCommon.setHomeAsUp(this);
        mToolbar.setNavigationOnClickListener(l -> {
            onBackPressed();
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, CreateItemFragment.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
