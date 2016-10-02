package com.acukanov.sibedge.ui.scaling.browse;


import android.net.Uri;

import com.acukanov.sibedge.ui.base.IPresenter;
import com.acukanov.sibedge.utils.LogUtils;

import java.io.IOException;

import javax.inject.Inject;

public class ScalingPresenter implements IPresenter<IScalingView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ScalingPresenter.class);
    private IScalingView mScalingView;

    @Inject
    ScalingPresenter() {}

    @Override
    public void attachView(IScalingView IView) {
        mScalingView = IView;
    }

    @Override
    public void detachView() {
        mScalingView = null;
    }

    public void loadImageIntoView(Uri imageUri) {
        try {
            mScalingView.onLoadImageIntoView(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
