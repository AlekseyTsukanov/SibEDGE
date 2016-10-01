package com.acukanov.sibedge.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.list.general.ListFragment;
import com.acukanov.sibedge.utils.ActivityCommon;
import com.acukanov.sibedge.utils.LogUtils;
import com.acukanov.sibedge.utils.PermissionsUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements IMainView {
    private static final String LOG_TAG = LogUtils.makeLogTag(MainActivity.class);
    private static final int REQUEST_CODE_PERMISSIONS = 0;
    @Inject MainPresenter mMainPresenter;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.navigation_view) NavigationView mNavigationView;
    @InjectView(R.id.drawer) DrawerLayout mDrawerLayout;
    private View mHeaderView;
    private String[] mPermissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static void startActivity(Activity activity, Fragment fragment) {
        Intent intent = new Intent(activity, MainActivity.class);
        if (fragment == null) {
            activity.startActivity(intent);
        } else {
            fragment.startActivity(intent);
        }
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mMainPresenter.attachView(this);

        setSupportActionBar(mToolbar);
        ActivityCommon.setHomeAsUp(this);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.partial_drawer_header, null);
        mNavigationView.addHeaderView(mHeaderView);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, ListFragment.newInstance())
                .commit();

        mNavigationView.setNavigationItemSelectedListener((menuItem) -> {
            if (menuItem.isChecked()) {
                menuItem.setChecked(false);
            } else {
                menuItem.setChecked(true);
            }
            mDrawerLayout.closeDrawers();
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.menu_drawer_list:
                    fragment = ListFragment.newInstance();
                    mMainPresenter.navigationItemSelected(fragment);
                    break;
                case R.id.menu_drawer_scaling:
                    break;
                case R.id.menu_drawer_service:
                    break;
                case R.id.menu_drawer_map:
                    break;
            }
            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.res_menu_drawer_open,
                R.string.res_menu_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionsUtils.hasPermission(this, Manifest.permission.INTERNET)
                || !PermissionsUtils.hasPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                || !PermissionsUtils.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                || !PermissionsUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionsUtils.requestPermissionsSafely(this, mPermissions, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    // MVP methods

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // TODO: implement.
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onNavigationItemSelected(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
    }
}
