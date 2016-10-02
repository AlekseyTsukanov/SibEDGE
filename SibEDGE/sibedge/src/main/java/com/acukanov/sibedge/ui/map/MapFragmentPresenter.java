package com.acukanov.sibedge.ui.map;


import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

public class MapFragmentPresenter implements IPresenter<IMapFragmentView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(MapFragmentPresenter.class);
    private IMapFragmentView mMapView;

    @Inject MapFragmentPresenter() {}

    @Override
    public void attachView(IMapFragmentView IView) {
        mMapView = IView;
    }

    @Override
    public void detachView() {
        mMapView = null;
    }

    public void initializeMap() {
        mMapView.onMapInitialize();
    }
}
