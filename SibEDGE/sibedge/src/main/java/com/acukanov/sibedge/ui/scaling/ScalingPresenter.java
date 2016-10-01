package com.acukanov.sibedge.ui.scaling;


import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

public class ScalingPresenter implements IPresenter<IScalingView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ScalingPresenter.class);
    private IScalingView mScalingView;

    @Inject ScalingPresenter() {}

    @Override
    public void attachView(IScalingView IView) {
        mScalingView = IView;
    }

    @Override
    public void detachView() {
        mScalingView = null;
    }
}
