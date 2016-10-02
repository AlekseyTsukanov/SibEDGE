package com.acukanov.sibedge.ui.service;


import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

public class ServicePresenter implements IPresenter<IServiceView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ServicePresenter.class);
    private IServiceView mServiceView;

    @Inject ServicePresenter() {}

    @Override
    public void attachView(IServiceView IView) {
        mServiceView = IView;
    }

    @Override
    public void detachView() {
        mServiceView = null;
    }

    public void startAsyncTaskLoader() {
        mServiceView.onStartLoader();
    }
}
