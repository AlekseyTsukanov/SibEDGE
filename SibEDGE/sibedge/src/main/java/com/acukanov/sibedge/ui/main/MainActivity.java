package com.acukanov.sibedge.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.data.preferences.LocalePreferenceManager;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.list.general.ListFragment;
import com.acukanov.sibedge.ui.map.GeoMapFragment;
import com.acukanov.sibedge.ui.scaling.general.PickerFragment;
import com.acukanov.sibedge.ui.service.ServiceFragment;
import com.acukanov.sibedge.utils.ActivityCommon;
import com.acukanov.sibedge.utils.LogUtils;
import com.acukanov.sibedge.utils.PermissionsUtils;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements IMainView {
    private static final String LOG_TAG = LogUtils.makeLogTag(MainActivity.class);
    public static final String INSTANCE_STATE_FRAGMENT_KEY = "instance_state_fragment_key";
    private static final int REQUEST_CODE_PERMISSIONS = 0;
    @Inject MainPresenter mMainPresenter;
    @Inject LocalePreferenceManager mLocaleManager;
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
    private Fragment mFragment = null;
    private FragmentManager mFragmentManager;
    private Locale mLocale;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_language:
                if (mLocaleManager.getLocale().equals("en")) {
                    setLocale("ru");
                    mLocaleManager.setLocale("ru");
                } else {
                    setLocale("en");
                    mLocaleManager.setLocale("en");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mMainPresenter.attachView(this);
        if (mLocaleManager.getLocale() == null) {
            setLocale("en");
            mLocaleManager.setLocale("en");
        }

        setSupportActionBar(mToolbar);
        ActivityCommon.setHomeAsUp(this);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.partial_drawer_header, null);
        mNavigationView.addHeaderView(mHeaderView);

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            if (mFragmentManager.getFragment(savedInstanceState, INSTANCE_STATE_FRAGMENT_KEY) == null) {
                mFragment = ListFragment.newInstance();
            } else {
                mFragment = mFragmentManager.getFragment(savedInstanceState, INSTANCE_STATE_FRAGMENT_KEY);
            }
        } else {
            mFragment = ListFragment.newInstance();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.main_content, mFragment)
                .commit();

        mNavigationView.setNavigationItemSelectedListener((menuItem) -> {
            if (menuItem.isChecked()) {
                menuItem.setChecked(false);
            } else {
                menuItem.setChecked(true);
            }
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.menu_drawer_list:
                    mFragment = ListFragment.newInstance();
                    mMainPresenter.navigationItemSelected(mFragment);
                    break;
                case R.id.menu_drawer_scaling:
                    mFragment = PickerFragment.newInstance();
                    mMainPresenter.navigationItemSelected(mFragment);
                    break;
                case R.id.menu_drawer_service:
                    mFragment = ServiceFragment.newInstance();
                    mMainPresenter.navigationItemSelected(mFragment);
                    break;
                case R.id.menu_drawer_map:
                    mFragment = GeoMapFragment.newInstance();
                    mMainPresenter.navigationItemSelected(mFragment);
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
            PermissionsUtils.requestPermissionsSafely(this, null, mPermissions, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, INSTANCE_STATE_FRAGMENT_KEY, mFragment);
        }
    }

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
            mFragmentManager.beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
    }

    private void setLocale(String locale) {
        mLocale = new Locale(locale);
        Resources resource = getResources();
        DisplayMetrics dm = resource.getDisplayMetrics();
        Configuration conf = resource.getConfiguration();
        conf.locale = mLocale;
        resource.updateConfiguration(conf, dm);
        Intent localeIntent = new Intent(this, MainActivity.class);
        localeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(localeIntent);
    }
}
