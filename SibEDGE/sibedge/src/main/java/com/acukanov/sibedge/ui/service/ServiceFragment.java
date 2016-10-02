package com.acukanov.sibedge.ui.service;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.acukanov.sibedge.R;
import com.acukanov.sibedge.data.remote.ServiceDataLoader;
import com.acukanov.sibedge.data.remote.model.ServiceData;
import com.acukanov.sibedge.ui.base.BaseActivity;
import com.acukanov.sibedge.ui.base.BaseFragment;
import com.acukanov.sibedge.utils.DialogFactory;
import com.acukanov.sibedge.utils.LogUtils;
import com.acukanov.sibedge.utils.PermissionsUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ServiceFragment extends BaseFragment implements IServiceView, LoaderManager.LoaderCallbacks<ArrayList<ServiceData>> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ServiceFragment.class);
    private static final int LOADER_REMOTE_DATA = 0;
    private static final int REQUEST_PERMISSION_INTERNET = 0;
    private Activity mActivity;
    @Inject ServicePresenter mServicePresenter;
    @Inject ServiceFragmentAdapter mServiceAdapter;
    @InjectView(R.id.service_list) RecyclerView mServiceList;
    @InjectView(R.id.swipe_refresh_container) SwipeRefreshLayout mSwipeContainer;
    @InjectView(R.id.progress_bar) ProgressBar mProgress;
    private LinearLayoutManager mLayoutManager;
    private ServiceDataLoader mServiceLoader;

    public ServiceFragment() {}

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).activityComponent().inject(this);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mServiceLoader = new ServiceDataLoader(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.inject(this, rootView);
        mServicePresenter.attachView(this);

        mServiceList.setLayoutManager(mLayoutManager);
        mServiceList.setHasFixedSize(true);
        mServiceList.setAdapter(mServiceAdapter);
        mSwipeContainer.setOnRefreshListener(() -> {
            getLoaderManager().restartLoader(LOADER_REMOTE_DATA, null, this);
            mSwipeContainer.setRefreshing(false);
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PermissionsUtils.hasPermission(mActivity, Manifest.permission.INTERNET)) {
            PermissionsUtils.requestPermissionsSafely(
                    mActivity,
                    this,
                    new String[] {Manifest.permission.INTERNET},
                    REQUEST_PERMISSION_INTERNET
            );
        } else {
            mServicePresenter.startAsyncTaskLoader();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServicePresenter.detachView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mServicePresenter.startAsyncTaskLoader();
                } else {
                    DialogFactory.createSimpleOkErrorDialog(
                            mActivity,
                            R.string.dialog_error_title,
                            R.string.dialog_message_internet_permission_error).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public Loader<ArrayList<ServiceData>> onCreateLoader(int id, Bundle args) {
        mProgress.setVisibility(View.VISIBLE);
        LogUtils.error(LOG_TAG, "Loader created");
        return mServiceLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ServiceData>> loader, ArrayList<ServiceData> data) {
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                LogUtils.error(LOG_TAG, i + "   " + data.toString());
            }
            mServiceAdapter.clearAdapter();
            mServiceAdapter.setServiceData(data);
            mServiceAdapter.notifyDataSetChanged();
        }
        mProgress.setVisibility(View.GONE);
        LogUtils.error(LOG_TAG, "Loader finished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ServiceData>> loader) {
        LogUtils.error(LOG_TAG, "Loader reseted");
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onStartLoader() {
        getLoaderManager().initLoader(LOADER_REMOTE_DATA, null, this);
    }
}
